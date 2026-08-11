package quynh.ecommerce.moonshop.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import quynh.ecommerce.moonshop.admin.product.dto.request.CreateProductRequest;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.category.repository.CategoryRepository;
import quynh.ecommerce.moonshop.common.PageResponse;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.product.dto.response.ProductResponse;
import quynh.ecommerce.moonshop.product.entity.Product;
import quynh.ecommerce.moonshop.product.mapper.ProductMapper;
import quynh.ecommerce.moonshop.product.repository.ProductRepository;
import quynh.ecommerce.moonshop.product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ProductMapper productMapper = mock(ProductMapper.class);
    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);

    @Test
    void publicListReturnsPagedActiveProducts() {
        Product product = product();
        ProductResponse response = ProductResponse.builder().id("p1").name("Ao").build();

        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));
        when(productMapper.toProductResponse(product)).thenReturn(response);

        PageResponse<ProductResponse> result = productService.getPublicProducts(1, 8, null, null, null, null, null);

        assertThat(result.getItems()).containsExactly(response);
        assertThat(result.getPage()).isEqualTo(1);
        verify(productRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void createProductRejectsMissingCategory() {
        CreateProductRequest request = createRequest();
        when(categoryRepository.findById("c1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOfSatisfying(AppException.class,
                        ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Test
    void createProductRejectsOriginalPriceBelowPrice() {
        CreateProductRequest request = createRequest();
        request.setOriginalPrice(BigDecimal.valueOf(90));

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOfSatisfying(AppException.class,
                        ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR));
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void deleteProductMarksProductInactive() {
        Product product = product();
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        productService.deleteProduct("p1");

        assertThat(product.getProductStatus()).isEqualTo(ProductStatus.INACTIVE);
        verify(productRepository).save(product);
    }

    private CreateProductRequest createRequest() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Ao dep");
        request.setPrice(BigDecimal.valueOf(100));
        request.setStock(10);
        request.setCategoryId("c1");
        return request;
    }

    private Product product() {
        return Product.builder()
                .id("p1")
                .name("Ao")
                .slug("ao")
                .price(BigDecimal.valueOf(100))
                .stock(10)
                .category(Category.builder().id("c1").name("Ao nu").build())
                .productStatus(ProductStatus.ACTIVE)
                .build();
    }
}
