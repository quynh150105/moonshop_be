package quynh.ecommerce.moonshop.address.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import quynh.ecommerce.moonshop.address.dto.request.CreateAddressRequest;
import quynh.ecommerce.moonshop.address.entity.Address;
import quynh.ecommerce.moonshop.address.repository.AddressRepository;
import quynh.ecommerce.moonshop.address.service.impl.AddressServiceImpl;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {
    private final AddressRepository addressRepository = mock(AddressRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AddressServiceImpl addressService = new AddressServiceImpl(addressRepository, userRepository);

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u1", null));
        when(userRepository.findById("u1")).thenReturn(Optional.of(User.builder().id("u1").build()));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createFirstAddressMakesItDefault() {
        when(addressRepository.findByUserIdOrderByCreatedAtDesc("u1")).thenReturn(List.of());

        var result = addressService.createAddress(request(false));

        assertThat(result.isDefaultAddress()).isTrue();
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void setDefaultClearsExistingDefaultAddress() {
        Address oldDefault = address("old", true);
        Address nextDefault = address("new", false);

        when(addressRepository.findByIdAndUserId("new", "u1")).thenReturn(Optional.of(nextDefault));
        when(addressRepository.findByUserIdAndDefaultAddressTrue("u1")).thenReturn(List.of(oldDefault));

        var result = addressService.setDefaultAddress("new");

        assertThat(oldDefault.isDefaultAddress()).isFalse();
        assertThat(result.isDefaultAddress()).isTrue();
    }

    @Test
    void updateRejectsAddressOwnedByAnotherUser() {
        when(addressRepository.findByIdAndUserId("a1", "u1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.updateAddress("a1", updateRequest()))
                .isInstanceOfSatisfying(AppException.class,
                        ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ADDRESS_NOT_FOUND));
    }

    private CreateAddressRequest request(boolean defaultAddress) {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setLabel("Home");
        request.setReceiverName("Nguyen Thu Ha");
        request.setPhone("0987654321");
        request.setLine("221B Le Duan");
        request.setDefaultAddress(defaultAddress);
        return request;
    }

    private quynh.ecommerce.moonshop.address.dto.request.UpdateAddressRequest updateRequest() {
        var request = new quynh.ecommerce.moonshop.address.dto.request.UpdateAddressRequest();
        request.setReceiverName("Nguyen Thu Ha");
        request.setPhone("0987654321");
        request.setLine("221B Le Duan");
        return request;
    }

    private Address address(String id, boolean defaultAddress) {
        return Address.builder()
                .id(id)
                .user(User.builder().id("u1").build())
                .receiverName("Nguyen Thu Ha")
                .phone("0987654321")
                .line("221B Le Duan")
                .defaultAddress(defaultAddress)
                .build();
    }
}
