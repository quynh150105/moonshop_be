package quynh.ecommerce.moonshop.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemResponse {
    private String productId;
    private String name;
    private String image;
    private BigDecimal price;
    private int quantity;
    private int stock;
    private boolean selected;
    private BigDecimal lineTotal;
}
