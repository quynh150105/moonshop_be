package quynh.ecommerce.moonshop.user.dto.response;

import lombok.*;
import quynh.ecommerce.moonshop.common.enums.Role;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;

    private String email;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private Role role;
}
