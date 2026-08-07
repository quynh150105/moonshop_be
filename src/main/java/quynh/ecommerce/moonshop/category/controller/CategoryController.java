package quynh.ecommerce.moonshop.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.category.dto.response.CategoryResponse;
import quynh.ecommerce.moonshop.category.service.CategoryService;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;

import java.util.List;

@RestApiV1
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(UrlConstant.Category.CATEGORIES)
    public ResponseEntity<List<CategoryResponse>> getPublicCategories() {
        return ResponseEntity.ok(categoryService.getPublicCategories());
    }

}
