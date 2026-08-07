package quynh.ecommerce.moonshop.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quynh.ecommerce.moonshop.admin.category.dto.request.CreateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.request.UpdateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.response.AdminCategoryResponse;
import quynh.ecommerce.moonshop.category.dto.response.CategoryResponse;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.category.mapper.CategoryMapper;
import quynh.ecommerce.moonshop.category.repository.CategoryRepository;
import quynh.ecommerce.moonshop.category.service.CategoryService;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.enums.CategoryStatus;
import quynh.ecommerce.moonshop.common.exception.AppException;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getPublicCategories() {
        return categoryMapper.toCategoryResponses(
                categoryRepository.findByCategoryStatusOrderByCreatedAtDesc(CategoryStatus.ACTIVE));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminCategoryResponse> getAdminCategories(String q) {
        List<Category> categories = q == null || q.isBlank()
                ? categoryRepository.findAllByOrderByCreatedAtDesc()
                : categoryRepository.findByNameContainingIgnoreCaseOrderByCreatedAtDesc(q.trim());
        return categoryMapper.toAdminCategoryResponses(categories);
    }

    @Override
    public AdminCategoryResponse createCategory(CreateCategoryRequest request) {
        String slug = toSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.CATEGORY_SLUG_ALREADY_EXISTS);
        }

        Category category = Category.builder()
                .name(request.getName().trim())
                .slug(slug)
                .description(request.getDescription())
                .icon(request.getIcon())
                .build();

        return categoryMapper.toAdminCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public AdminCategoryResponse updateCategory(String id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getName() != null) {
            String name = request.getName().trim();
            String slug = toSlug(name);
            if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
                throw new AppException(ErrorCode.CATEGORY_SLUG_ALREADY_EXISTS);
            }
            category.setName(name);
            category.setSlug(slug);
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getStatus() != null) {
            category.setCategoryStatus(parseStatus(request.getStatus()));
        }

        return categoryMapper.toAdminCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    private CategoryStatus parseStatus(String status) {
        try {
            return CategoryStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.VALIDATION_ERROR);
        }
    }

    private String toSlug(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('đ', 'd')
                .replace('Đ', 'D')
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        return normalized.isBlank() ? "category" : normalized;
    }
}
