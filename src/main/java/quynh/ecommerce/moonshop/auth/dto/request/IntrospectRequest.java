package quynh.ecommerce.moonshop.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import quynh.ecommerce.moonshop.common.constanst.ErrorMessage;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectRequest {
    @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
    String token;
}

