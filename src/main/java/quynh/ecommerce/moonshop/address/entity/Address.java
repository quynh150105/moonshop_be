package quynh.ecommerce.moonshop.address.entity;

import jakarta.persistence.*;
import lombok.*;
import quynh.ecommerce.moonshop.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "addresses",
        indexes = @Index(name = "idx_addresses_user_default", columnList = "user_id, is_default")
)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "label")
    private String label;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address_line", nullable = false)
    private String line;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean defaultAddress = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void handlePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void handleUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
