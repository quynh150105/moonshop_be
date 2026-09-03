package quynh.ecommerce.moonshop.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartResponse {
    private List<CartItemResponse> items;
    private int itemCount;
    private int selectedCount;
    private BigDecimal subtotal;
    private BigDecimal selectedSubtotal;
}
