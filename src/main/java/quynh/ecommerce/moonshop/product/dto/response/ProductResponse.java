package quynh.ecommerce.moonshop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private List<String> images;
    private String category;
    private String categoryId;
    private double rating;
    private long reviewCount;
    private long soldCount;
    private int stock;
    @JsonProperty("isNew")
    private boolean isNew;
    @JsonProperty("isFeatured")
    private boolean isFeatured;
    @JsonProperty("isBestSeller")
    private boolean isBestSeller;
}
