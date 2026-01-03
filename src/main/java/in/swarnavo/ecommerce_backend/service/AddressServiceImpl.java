package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import in.swarnavo.ecommerce_backend.model.Address;
import in.swarnavo.ecommerce_backend.model.User;
import in.swarnavo.ecommerce_backend.repository.AddressRepository;
import in.swarnavo.ecommerce_backend.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final AuthUtil authUtil;

    private final ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);

        User user = authUtil.loggedInUser();

        List<Address> addressList = user.getAddresses();
        addressList.add(address);

        user.setAddresses(addressList);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
