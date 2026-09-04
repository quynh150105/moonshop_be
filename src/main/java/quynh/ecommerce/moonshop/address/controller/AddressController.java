package quynh.ecommerce.moonshop.address.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quynh.ecommerce.moonshop.address.dto.request.CreateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.request.UpdateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.response.AddressResponse;
import quynh.ecommerce.moonshop.address.service.AddressService;
import quynh.ecommerce.moonshop.common.base.RestApiV1;
import quynh.ecommerce.moonshop.common.constanst.UrlConstant;

import java.util.List;

@RestApiV1
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping(UrlConstant.Address.ADDRESSES)
    public ResponseEntity<List<AddressResponse>> getAddresses() {
        return ResponseEntity.ok(addressService.getAddresses());
    }

    @PostMapping(UrlConstant.Address.ADDRESSES)
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(addressService.createAddress(request));
    }

    @PutMapping(UrlConstant.Address.ADDRESS_BY_ID)
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable String id,
                                                         @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }

    @DeleteMapping(UrlConstant.Address.ADDRESS_BY_ID)
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(UrlConstant.Address.DEFAULT_ADDRESS)
    public ResponseEntity<AddressResponse> setDefaultAddress(@PathVariable String id) {
        return ResponseEntity.ok(addressService.setDefaultAddress(id));
    }
}
