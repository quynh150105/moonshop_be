package quynh.ecommerce.moonshop.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import quynh.ecommerce.moonshop.common.enums.CartStatus;
import quynh.ecommerce.moonshop.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "carts",
        indexes = @Index(name = "idx_carts_user_status", columnList = "user_id, cart_status")
)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cart_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CartStatus status = CartStatus.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Optional<CartItem> findItem(String productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
    }

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
