package quynh.ecommerce.moonshop.address.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quynh.ecommerce.moonshop.address.dto.request.CreateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.request.UpdateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.response.AddressResponse;
import quynh.ecommerce.moonshop.address.entity.Address;
import quynh.ecommerce.moonshop.address.repository.AddressRepository;
import quynh.ecommerce.moonshop.address.service.AddressService;
import quynh.ecommerce.moonshop.common.constanst.ErrorCode;
import quynh.ecommerce.moonshop.common.exception.AppException;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses() {
        return addressRepository.findByUserIdOrderByCreatedAtDesc(currentUserId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request) {
        User user = getAuthenticatedUser();
        boolean makeDefault = Boolean.TRUE.equals(request.getDefaultAddress())
                || addressRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).isEmpty();
        if (makeDefault) {
            clearDefault(user.getId());
        }

        Address address = Address.builder()
                .user(user)
                .label(request.getLabel())
                .receiverName(request.getReceiverName())
                .phone(request.getPhone())
                .line(request.getLine())
                .defaultAddress(makeDefault)
                .build();
        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(String id, UpdateAddressRequest request) {
        String userId = currentUserId();
        Address address = findOwnedAddress(id, userId);
        if (Boolean.TRUE.equals(request.getDefaultAddress())) {
            clearDefault(userId);
        }
        address.setLabel(request.getLabel());
        address.setReceiverName(request.getReceiverName());
        address.setPhone(request.getPhone());
        address.setLine(request.getLine());
        if (request.getDefaultAddress() != null) {
            address.setDefaultAddress(request.getDefaultAddress());
        }
        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(String id) {
        String userId = currentUserId();
        Address address = findOwnedAddress(id, userId);
        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(String id) {
        String userId = currentUserId();
        Address address = findOwnedAddress(id, userId);
        clearDefault(userId);
        address.setDefaultAddress(true);
        return toResponse(addressRepository.save(address));
    }

    private User getAuthenticatedUser() {
        return userRepository.findById(currentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private String currentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Address findOwnedAddress(String id, String userId) {
        return addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    private void clearDefault(String userId) {
        addressRepository.findByUserIdAndDefaultAddressTrue(userId).forEach(address -> address.setDefaultAddress(false));
    }

    private AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .label(address.getLabel())
                .receiverName(address.getReceiverName())
                .phone(address.getPhone())
                .line(address.getLine())
                .defaultAddress(address.isDefaultAddress())
                .build();
    }
}
