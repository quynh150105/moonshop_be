package quynh.ecommerce.moonshop.product.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductSearchResponse {
    private String id;
    private String name;
    private String slug;
    private BigDecimal price;
    private List<String> images;
    private int stock;
}
