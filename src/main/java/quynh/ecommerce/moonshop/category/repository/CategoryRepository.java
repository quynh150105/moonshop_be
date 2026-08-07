package quynh.ecommerce.moonshop.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quynh.ecommerce.moonshop.category.entity.Category;
import quynh.ecommerce.moonshop.common.enums.CategoryStatus;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, String id);

    List<Category> findByCategoryStatusOrderByCreatedAtDesc(CategoryStatus status);

    List<Category> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name);

    List<Category> findAllByOrderByCreatedAtDesc();
}
