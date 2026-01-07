package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import in.swarnavo.ecommerce_backend.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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


    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Create Address", description = "API to create address")
    @PostMapping("/addresses")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddress = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get User Addresses", description = "API to get user addresses")
    @GetMapping("/addresses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddresses();
        return ResponseEntity.ok(addressDTOList);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get Address By Id", description = "API to get address by id")
    @GetMapping("/addresses/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDTO);
    }


    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Get User Address", description = "API to get user address")
    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        List<AddressDTO> userAddresses = addressService.getUserAddresses();
        return ResponseEntity.ok(userAddresses);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Update Address", description = "API to update address")
    @PutMapping("/addresses/{addressId}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @Tag(name = "Address APIs", description = "APIs for managing addresses")
    @Operation(summary = "Delete Address", description = "API to delete address")
    @DeleteMapping("/addresses/{addressId}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId) {
        AddressDTO deletedAddress = addressService.deleteAddress(addressId);
        return ResponseEntity.ok(deletedAddress);
    }
}
