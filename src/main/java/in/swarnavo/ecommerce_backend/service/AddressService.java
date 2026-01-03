package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import jakarta.validation.Valid;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO);
}
