package quynh.ecommerce.moonshop.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ErrorMessage.INVALID_FORMAT_EMAIL)
    private String email;

    @Size(min = 8, message = ErrorMessage.INVALID_FORMAT_PASSWORD)
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String password;
}
