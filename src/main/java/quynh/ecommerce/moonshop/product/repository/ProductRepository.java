package quynh.ecommerce.moonshop.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import quynh.ecommerce.moonshop.common.enums.ProductStatus;
import quynh.ecommerce.moonshop.product.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndProductStatus(String id, ProductStatus status);

    Optional<Product> findBySlugAndProductStatus(String slug, ProductStatus status);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, String id);

    @Query("""
            select p.name
            from Product p
            where p.productStatus = :status
              and (:q is null or lower(p.name) like lower(concat('%', :q, '%')))
            order by p.name asc
            """)
    List<String> findTop10NamesByStatusAndQuery(@Param("status") ProductStatus status, @Param("q") String q);
}
