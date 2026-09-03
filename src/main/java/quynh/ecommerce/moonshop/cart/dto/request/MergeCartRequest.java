package quynh.ecommerce.moonshop.cart.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MergeCartRequest {
    @Valid
    private List<AddCartItemRequest> items = new ArrayList<>();
}
