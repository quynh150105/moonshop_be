package quynh.ecommerce.moonshop.product.service;

import quynh.ecommerce.moonshop.admin.product.dto.request.CreateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.request.UpdateProductRequest;
import quynh.ecommerce.moonshop.admin.product.dto.response.AdminProductResponse;
import quynh.ecommerce.moonshop.common.PageResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductDetailResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductSearchResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    PageResponse<ProductResponse> getPublicProducts(int page, int size, String category, String sort,
                                                    String q, BigDecimal minPrice, BigDecimal maxPrice);

    ProductDetailResponse getProductDetail(String idOrSlug);

    List<ProductSearchResponse> searchProducts(String q);

    List<String> getSuggestions(String q);

    PageResponse<AdminProductResponse> getAdminProducts(int page, int size, String q, String categoryId, String sort);

    AdminProductResponse createProduct(CreateProductRequest request);

    AdminProductResponse updateProduct(String id, UpdateProductRequest request);

    void deleteProduct(String id);
}
