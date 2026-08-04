package quynh.ecommerce.moonshop.user.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String avatarPublicId;
}
