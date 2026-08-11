package quynh.ecommerce.moonshop.admin.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class AdminProductResponse {
    private String id;
    private String name;
    private String description;
    private String image;
    private String imagePublicId;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private int stock;
    private String categoryId;
    private String categoryName;
    private ProductStatus status;
    private LocalDate createdAt;
    @JsonProperty("isNew")
    private boolean isNew;
    @JsonProperty("isFeatured")
    private boolean isFeatured;
    @JsonProperty("isBestSeller")
    private boolean isBestSeller;
}
