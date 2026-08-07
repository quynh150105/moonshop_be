package quynh.ecommerce.moonshop.category.service;

import quynh.ecommerce.moonshop.admin.category.dto.request.CreateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.request.UpdateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.response.AdminCategoryResponse;
import quynh.ecommerce.moonshop.category.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getPublicCategories();

    List<AdminCategoryResponse> getAdminCategories(String q);

    AdminCategoryResponse createCategory(CreateCategoryRequest request);

    AdminCategoryResponse updateCategory(String id, UpdateCategoryRequest request);

    void deleteCategory(String id);
}
