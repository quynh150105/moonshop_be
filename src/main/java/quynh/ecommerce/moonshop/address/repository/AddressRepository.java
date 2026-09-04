package quynh.ecommerce.moonshop.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quynh.ecommerce.moonshop.address.entity.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Address> findByIdAndUserId(String id, String userId);

    List<Address> findByUserIdAndDefaultAddressTrue(String userId);
}
