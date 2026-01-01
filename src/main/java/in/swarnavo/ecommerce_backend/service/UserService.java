package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.LoginDTO;
import in.swarnavo.ecommerce_backend.dto.LoginResponse;
import in.swarnavo.ecommerce_backend.dto.RegisterDTO;
import in.swarnavo.ecommerce_backend.dto.UserDTO;
import jakarta.validation.Valid;

public interface UserService {
    UserDTO register(@Valid RegisterDTO request);

    LoginResponse login(@Valid LoginDTO request);
}
