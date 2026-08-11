package quynh.ecommerce.moonshop.product.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quynh.ecommerce.moonshop.admin.product.dto.request.CreateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.request.UpdateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.response.AdminProductResponse;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.category.repository.CategoryRepository;
import quynh.ecommerce.moonshop.common.PageResponse;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.product.dto.response.ProductDetailResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductSearchResponse;
import quynh.ecommerce.moonshop.product.entity.Product;
import quynh.ecommerce.moonshop.product.mapper.ProductMapper;
import quynh.ecommerce.moonshop.product.repository.ProductRepository;
import quynh.ecommerce.moonshop.product.service.ProductService;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final int MAX_PAGE_SIZE = 50;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getPublicProducts(int page, int size, String category, String sort,
                                                           String q, BigDecimal minPrice, BigDecimal maxPrice) {
        Page<ProductResponse> products = productRepository.findAll(
                publicSpec(category, q, minPrice, maxPrice),
                pageable(page, size, publicSort(sort))
        ).map(productMapper::toProductResponse);
        return PageResponse.from(products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(String idOrSlug) {
        Product product = productRepository.findByIdAndProductStatus(idOrSlug, ProductStatus.ACTIVE)
                .or(() -> productRepository.findBySlugAndProductStatus(idOrSlug, ProductStatus.ACTIVE))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductDetailResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSearchResponse> searchProducts(String q) {
        return productRepository.findAll(publicSpec(null, q, null, null), pageable(1, 10, publicSort("newest")))
                .map(productMapper::toProductSearchResponse)
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSuggestions(String q) {
        return productRepository.findTop10NamesByStatusAndQuery(ProductStatus.ACTIVE, clean(q)).stream()
                .limit(10)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdminProductResponse> getAdminProducts(int page, int size, String q, String categoryId, String sort) {
        Page<AdminProductResponse> products = productRepository.findAll(
                adminSpec(q, categoryId),
                pageable(page, size, adminSort(sort))
        ).map(productMapper::toAdminProductResponse);
        return PageResponse.from(products);
    }

    @Override
    @Transactional
    public AdminProductResponse createProduct(CreateProductRequest request) {
        validatePrices(request.getPrice(), request.getOriginalPrice());
        String name = request.getName().trim();
        String slug = toSlug(name);
        if (productRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_ALREADY_EXISTS);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = Product.builder()
                .name(name)
                .slug(slug)
                .description(request.getDescription())
                .image(request.getImage())
                .imagePublicId(request.getImagePublicId())
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice())
                .stock(request.getStock())
                .category(category)
                .productStatus(parseStatus(request.getStatus(), ProductStatus.ACTIVE))
                .newProduct(Boolean.TRUE.equals(request.getIsNew()))
                .featured(Boolean.TRUE.equals(request.getIsFeatured()))
                .bestSeller(Boolean.TRUE.equals(request.getIsBestSeller()))
                .build();
        return productMapper.toAdminProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public AdminProductResponse updateProduct(String id, UpdateProductRequest request) {
        Product product = findProduct(id);

        if (request.getName() != null) {
            String name = request.getName().trim();
            String slug = toSlug(name);
            if (productRepository.existsBySlugAndIdNot(slug, id)) {
                throw new AppException(ErrorCode.PRODUCT_SLUG_ALREADY_EXISTS);
            }
            product.setName(name);
            product.setSlug(slug);
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getImage() != null) {
            product.setImage(request.getImage());
        }
        if (request.getImagePublicId() != null) {
            product.setImagePublicId(request.getImagePublicId());
        }
        if (request.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getOriginalPrice() != null) {
            product.setOriginalPrice(request.getOriginalPrice());
        }
        validatePrices(product.getPrice(), product.getOriginalPrice());
        if (request.getStock() != 0) {
            product.setStock(request.getStock());
        }
        if (request.getStatus() != null) {
            product.setProductStatus(parseStatus(request.getStatus(), product.getProductStatus()));
        }
        if (request.getIsNew() != null) {
            product.setNewProduct(request.getIsNew());
        }
        if (request.getIsFeatured() != null) {
            product.setFeatured(request.getIsFeatured());
        }
        if (request.getIsBestSeller() != null) {
            product.setBestSeller(request.getIsBestSeller());
        }

        return productMapper.toAdminProductResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = findProduct(id);
        product.setProductStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    private Product findProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Specification<Product> publicSpec(String categoryId, String q, BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("productStatus"), ProductStatus.ACTIVE));
            if (categoryId != null && !categoryId.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId.trim()));
            }
            addQueryPredicate(q, root.get("name"), criteriaBuilder, predicates);
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<Product> adminSpec(String q, String categoryId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addQueryPredicate(q, root.get("name"), criteriaBuilder, predicates);
            if (categoryId != null && !categoryId.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId.trim()));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void addQueryPredicate(String q, jakarta.persistence.criteria.Path<String> path,
                                   jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                                   List<Predicate> predicates) {
        String queryText = clean(q);
        if (queryText != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + queryText.toLowerCase(Locale.ROOT) + "%"));
        }
    }

    private Pageable pageable(int page, int size, Sort sort) {
        int pageIndex = Math.max(page, 1) - 1;
        int pageSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(pageIndex, pageSize, sort);
    }

    private Sort publicSort(String sort) {
        return switch (sort == null ? "" : sort) {
            case "price-asc" -> Sort.by("price").ascending();
            case "price-desc" -> Sort.by("price").descending();
            case "bestselling" -> Sort.by("soldCount").descending();
            case "discount" -> Sort.by("originalPrice").descending();
            default -> Sort.by("createdAt").descending();
        };
    }

    private Sort adminSort(String sort) {
        return switch (sort == null ? "" : sort) {
            case "price-asc" -> Sort.by("price").ascending();
            case "price-desc" -> Sort.by("price").descending();
            case "stock-asc" -> Sort.by("stock").ascending();
            case "name" -> Sort.by("name").ascending();
            default -> Sort.by("createdAt").descending();
        };
    }

    private ProductStatus parseStatus(String status, ProductStatus defaultStatus) {
        if (status == null || status.isBlank()) {
            return defaultStatus;
        }
        try {
            return ProductStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR);
        }
    }

    private void validatePrices(BigDecimal price, BigDecimal originalPrice) {
        if (originalPrice != null && price != null && originalPrice.compareTo(price) < 0) {
            throw new AppException(ErrorCode.VALIDATION_ERROR);
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String toSlug(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('\u0111', 'd')
                .replace('\u0110', 'D')
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        return normalized.isBlank() ? "product" : normalized;
    }
}
