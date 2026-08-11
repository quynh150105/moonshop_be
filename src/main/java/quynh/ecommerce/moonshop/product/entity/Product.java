package quynh.ecommerce.moonshop.product.entity;

import jakarta.persistence.*;
import lombok.*;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_status_created_at", columnList = "product_status, created_at"),
                @Index(name = "idx_products_category_status", columnList = "category_id, product_status"),
                @Index(name = "idx_products_name", columnList = "product_name")
        }
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "product_description", length = 2000)
    private String description;

    @Column(name = "image_url")
    private String image;

    @Column(name = "image_public_id")
    private String imagePublicId;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 15, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "product_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductStatus productStatus = ProductStatus.ACTIVE;

    @Column(name = "rating")
    @Builder.Default
    private double rating = 0;

    @Column(name = "review_count")
    @Builder.Default
    private long reviewCount = 0;

    @Column(name = "sold_count")
    @Builder.Default
    private long soldCount = 0;

    @Column(name = "is_new")
    @Builder.Default
    private boolean newProduct = false;

    @Column(name = "is_featured")
    @Builder.Default
    private boolean featured = false;

    @Column(name = "is_best_seller")
    @Builder.Default
    private boolean bestSeller = false;

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
