package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.RegisterRequest;
import in.swarnavo.ecommerce_backend.dto.UserResponse;
import jakarta.validation.Valid;

public interface UserService {
    UserResponse register(@Valid RegisterRequest request);
}
