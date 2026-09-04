package quynh.ecommerce.moonshop.address.service;

import quynh.ecommerce.moonshop.address.dto.request.CreateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.request.UpdateAddressRequest;
import quynh.ecommerce.moonshop.address.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    List<AddressResponse> getAddresses();

    AddressResponse createAddress(CreateAddressRequest request);

    AddressResponse updateAddress(String id, UpdateAddressRequest request);

    void deleteAddress(String id);

    AddressResponse setDefaultAddress(String id);
}
