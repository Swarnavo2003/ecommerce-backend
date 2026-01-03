package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import in.swarnavo.ecommerce_backend.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/user/addresses")
//    @PreAuthorize("hasRole('USER','SELLER','ADMIN')")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddress = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddresses();
        return ResponseEntity.ok(addressDTOList);
    }

    @GetMapping("/addresses/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        List<AddressDTO> userAddresses = addressService.getUserAddresses();
        return ResponseEntity.ok(userAddresses);
    }
}
