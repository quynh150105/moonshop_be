package quynh.ecommerce.moonshop.auth.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private AuthUserResponse user;
    private String token;
    private String refreshToken;
    private long expiresAt;
}
