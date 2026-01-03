package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);
}
