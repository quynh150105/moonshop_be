package quynh.ecommerce.moonshop.admin.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.admin.category.dto.request.CreateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.request.UpdateCategoryRequest;
import quynh.ecommerce.moonshop.admin.category.dto.response.AdminCategoryResponse;
import quynh.ecommerce.moonshop.category.service.CategoryService;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;

import java.net.URI;
import java.util.List;

@RestApiV1
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping(UrlConstant.Admin.CATEGORIES)
    public ResponseEntity<List<AdminCategoryResponse>> getAdminCategories(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(categoryService.getAdminCategories(q));
    }

    @PostMapping(UrlConstant.Admin.CATEGORIES)
    public ResponseEntity<AdminCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        AdminCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create("/api" + UrlConstant.Admin.CATEGORIES + "/" + response.getId())).body(response);
    }

    @PutMapping(UrlConstant.Admin.CATEGORY_BY_ID)
    public ResponseEntity<AdminCategoryResponse> updateCategory(@PathVariable String id,
                                                                @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping(UrlConstant.Admin.CATEGORY_BY_ID)
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
