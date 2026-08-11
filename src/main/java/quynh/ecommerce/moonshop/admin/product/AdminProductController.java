package quynh.ecommerce.moonshop.admin.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.admin.product.dto.request.CreateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.request.UpdateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.response.AdminProductResponse;
import quynh.ecommerce.moonshop.common.PageResponse;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;
import quynh.ecommerce.moonshop.product.service.ProductService;

import java.net.URI;

@RestApiV1
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping(UrlConstant.Admin.PRODUCTS)
    public ResponseEntity<PageResponse<AdminProductResponse>> getProducts(@RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(required = false) String q,
                                                                          @RequestParam(required = false) String categoryId,
                                                                          @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(productService.getAdminProducts(page, size, q, categoryId, sort));
    }

    @PostMapping(UrlConstant.Admin.PRODUCTS)
    public ResponseEntity<AdminProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        AdminProductResponse response = productService.createProduct(request);
        return ResponseEntity.created(URI.create("/api" + UrlConstant.Admin.PRODUCTS + "/" + response.getId())).body(response);
    }

    @PutMapping(UrlConstant.Admin.PRODUCT_BY_ID)
    public ResponseEntity<AdminProductResponse> updateProduct(@PathVariable String id,
                                                              @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping(UrlConstant.Admin.PRODUCT_BY_ID)
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
