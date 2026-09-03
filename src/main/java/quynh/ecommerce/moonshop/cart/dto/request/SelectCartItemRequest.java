package quynh.ecommerce.moonshop.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectCartItemRequest {
    @NotNull(message = "Selected is required")
    private Boolean selected;
}
