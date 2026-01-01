package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.RegisterRequest;
import in.swarnavo.ecommerce_backend.dto.UserResponse;
import in.swarnavo.ecommerce_backend.model.User;
import in.swarnavo.ecommerce_backend.model.enums.UserRole;
import in.swarnavo.ecommerce_backend.repository.UserRepository;
import in.swarnavo.ecommerce_backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final ModelMapper modelMapper;

    @Override
    public UserResponse register(RegisterRequest request) {
        User user = modelMapper.map(request, User.class);

        user.setRole(request.getRole() == null ? UserRole.USER : UserRole.valueOf(request.getRole()));

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }
}
