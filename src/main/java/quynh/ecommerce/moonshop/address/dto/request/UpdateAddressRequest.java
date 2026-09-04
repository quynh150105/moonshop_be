package quynh.ecommerce.moonshop.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequest {
    private String label;

    @NotBlank(message = "Receiver name is required")
    private String receiverName;

    @NotBlank(message = "Phone is required")
    @Size(min = 9, max = 15, message = "Phone must be between 9 and 15 characters")
    private String phone;

    @NotBlank(message = "Address is required")
    private String line;

    private Boolean defaultAddress;
}
