package quynh.ecommerce.moonshop.auth.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectResponse {
    private boolean valid;
}
