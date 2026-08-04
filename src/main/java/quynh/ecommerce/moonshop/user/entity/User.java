package quynh.ecommerce.moonshop.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import quynh.ecommerce.moonshop.common.enums.Role;
import quynh.ecommerce.moonshop.common.enums.UserStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id", unique = true, nullable = false)
    private String id;

    @Column(name="email", unique = true, nullable = false)
    @Email
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="avatar_url")
    private String avatarUrl;

    @Column(name="avatar_public_id")
    private String avatarPublicId;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name="user_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void handlePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void handleUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
