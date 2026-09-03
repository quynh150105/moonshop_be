package quynh.ecommerce.moonshop.cart.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quynh.ecommerce.moonshop.cart.entity.Cart;
import quynh.ecommerce.moonshop.common.enums.CartStatus;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Cart> findByUserIdAndStatus(String userId, CartStatus status);
}
