package quynh.ecommerce.moonshop.category.entity;

import jakarta.persistence.*;
import lombok.*;
import quynh.ecommerce.moonshop.common.enums.CategoryStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name="categories",
        indexes = {
                @Index(name = "idx_categories_status_created_at", columnList = "category_status, created_at"),
                @Index(name = "idx_categories_name", columnList = "category_name")
        }
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="categories_id")
    private String id;

    @Column(name="category_name", nullable = false)
    private String name;

    @Column(name= "category_slug", nullable = false, unique = true)
    private String slug;

    @Column(name= "category_description")
    private String description;

    @Column(name="category_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CategoryStatus categoryStatus = CategoryStatus.ACTIVE;

    @Column(name="category_icon")
    private String icon;

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
