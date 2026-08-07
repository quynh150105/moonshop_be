package quynh.ecommerce.moonshop.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import quynh.ecommerce.moonshop.admin.category.dto.response.AdminCategoryResponse;
import quynh.ecommerce.moonshop.category.dto.response.CategoryResponse;
import quynh.ecommerce.moonshop.category.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "productCount", constant = "0L")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "status", source = "categoryStatus")
    @Mapping(target = "createdAt", expression = "java(category.getCreatedAt().toLocalDate())")
    @Mapping(target = "productCount", constant = "0L")
    AdminCategoryResponse toAdminCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponses(List<Category> categories);

    List<AdminCategoryResponse> toAdminCategoryResponses(List<Category> categories);
}
