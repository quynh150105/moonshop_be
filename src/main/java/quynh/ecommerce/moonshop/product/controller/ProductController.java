package quynh.ecommerce.moonshop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import quynh.ecommerce.moonshop.common.PageResponse;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;
import quynh.ecommerce.moonshop.product.dto.response.ProductDetailResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductSearchResponse;
import quynh.ecommerce.moonshop.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestApiV1
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(UrlConstant.Product.PRODUCTS)
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getPublicProducts(page, size, category, sort, q, minPrice, maxPrice));
    }

    @GetMapping(UrlConstant.Product.SEARCH)
    public ResponseEntity<List<ProductSearchResponse>> searchProducts(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(productService.searchProducts(q));
    }

    @GetMapping(UrlConstant.Product.SUGGESTIONS)
    public ResponseEntity<List<String>> getSuggestions(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(productService.getSuggestions(q));
    }

    @GetMapping(UrlConstant.Product.PRODUCT_BY_ID_OR_SLUG)
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(productService.getProductDetail(idOrSlug));
    }
}
