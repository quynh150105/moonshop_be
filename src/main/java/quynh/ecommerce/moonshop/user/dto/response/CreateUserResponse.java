package quynh.ecommerce.moonshop.user.dto.response;

import lombok.*;
import quynh.ecommerce.moonshop.common.enums.Role;
import quynh.ecommerce.moonshop.common.enums.UserStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private String id;

    private String email;

    private String fullName;

    private String phone;

    private String avatar;

    private Role role;

}
