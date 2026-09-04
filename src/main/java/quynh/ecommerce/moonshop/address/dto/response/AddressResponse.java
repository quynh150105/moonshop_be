package quynh.ecommerce.moonshop.address.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {
    private String id;
    private String label;
    private String receiverName;
    private String phone;
    private String line;
    private boolean defaultAddress;
}
