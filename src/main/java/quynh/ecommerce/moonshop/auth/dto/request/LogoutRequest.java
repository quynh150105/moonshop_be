package quynh.ecommerce.moonshop.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String refreshToken;
}
