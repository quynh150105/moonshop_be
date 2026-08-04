package quynh.ecommerce.moonshop.auth.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
    private long expiresAt;
}
