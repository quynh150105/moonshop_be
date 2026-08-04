package quynh.ecommerce.moonshop.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = ErrorMessage.INVALID_FORMAT_EMAIL)
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String email;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    @Size(min = 8, message = ErrorMessage.INVALID_FORMAT_PASSWORD)
    private String password;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    @Size(min = 2, message = ErrorMessage.INVALID_FORMAT_FULL_NAME)
    private String fullName;

    @Size(min = 9, max = 15, message = ErrorMessage.INVALID_SOME_THING_FIELD)
    private String phone;

}
