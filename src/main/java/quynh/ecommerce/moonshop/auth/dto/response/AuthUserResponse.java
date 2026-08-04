package quynh.ecommerce.moonshop.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import quynh.ecommerce.moonshop.common.enums.Role;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserResponse {
    private String id;
    private String email;
    private String fullName;
    private Role role;
    private String avatar;
}
