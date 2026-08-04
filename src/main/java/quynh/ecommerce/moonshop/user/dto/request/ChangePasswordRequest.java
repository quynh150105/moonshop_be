package quynh.ecommerce.moonshop.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    private String current;

    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    @Size(min = 8, message = ErrorMessage.INVALID_FORMAT_PASSWORD)
    private String next;
}
