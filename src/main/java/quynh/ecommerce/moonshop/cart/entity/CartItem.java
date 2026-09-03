package quynh.ecommerce.moonshop.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import quynh.ecommerce.moonshop.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(name = "uk_cart_items_cart_product", columnNames = {"cart_id", "product_id"})
)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_item_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "selected", nullable = false)
    @Builder.Default
    private boolean selected = true;

    @Column(name = "price_snapshot", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceSnapshot;

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
