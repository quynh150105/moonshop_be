package quynh.ecommerce.moonshop.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {
    @NotBlank(message = "Product is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity = 1;
}
