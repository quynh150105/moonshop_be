package quynh.ecommerce.moonshop.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequest {
    @Size(min = 2, message = ErrorMessage.INVALID_FORMAT_FULL_NAME)
    private String fullName;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = ErrorMessage.INVALID_FORMAT_EMAIL)
    private String email;

    @Size(min = 9, max = 15, message = ErrorMessage.INVALID_SOME_THING_FIELD)
    private String phone;

    @Size(min = 5, message = ErrorMessage.INVALID_SOME_THING_FIELD)
    private String address;

    private String avatar;

    private String avatarPublicId;
}
