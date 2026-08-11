package quynh.ecommerce.moonshop.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import quynh.ecommerce.moonshop.admin.product.dto.response.AdminProductResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductDetailResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductResponse;
import quynh.ecommerce.moonshop.product.dto.response.ProductSearchResponse;
import quynh.ecommerce.moonshop.product.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "images", source = "product", qualifiedByName = "toImages")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "isNew", source = "newProduct")
    @Mapping(target = "isFeatured", source = "featured")
    @Mapping(target = "isBestSeller", source = "bestSeller")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "images", source = "product", qualifiedByName = "toImages")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "isNew", source = "newProduct")
    @Mapping(target = "isFeatured", source = "featured")
    @Mapping(target = "isBestSeller", source = "bestSeller")
    @Mapping(target = "specifications", expression = "java(emptySpecifications())")
    ProductDetailResponse toProductDetailResponse(Product product);

    @Mapping(target = "images", source = "product", qualifiedByName = "toImages")
    ProductSearchResponse toProductSearchResponse(Product product);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "status", source = "productStatus")
    @Mapping(target = "createdAt", expression = "java(product.getCreatedAt().toLocalDate())")
    @Mapping(target = "isNew", source = "newProduct")
    @Mapping(target = "isFeatured", source = "featured")
    @Mapping(target = "isBestSeller", source = "bestSeller")
    AdminProductResponse toAdminProductResponse(Product product);

    @Named("toImages")
    default List<String> toImages(Product product) {
        return product.getImage() == null || product.getImage().isBlank()
                ? Collections.emptyList()
                : List.of(product.getImage());
    }

    default Map<String, String> emptySpecifications() {
        return Map.of();
    }
}
