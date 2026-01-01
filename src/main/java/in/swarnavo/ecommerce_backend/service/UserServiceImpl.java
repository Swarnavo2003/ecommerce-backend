package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.LoginDTO;
import in.swarnavo.ecommerce_backend.dto.LoginResponse;
import in.swarnavo.ecommerce_backend.dto.RegisterDTO;
import in.swarnavo.ecommerce_backend.dto.UserDTO;
import in.swarnavo.ecommerce_backend.model.User;
import in.swarnavo.ecommerce_backend.model.enums.UserRole;
import in.swarnavo.ecommerce_backend.repository.UserRepository;
import in.swarnavo.ecommerce_backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.AuthenticationException;
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
    public UserDTO register(RegisterDTO request) {
        User user = modelMapper.map(request, User.class);

        user.setRole(request.getRole() == null ? UserRole.USER : UserRole.valueOf(request.getRole()));

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public LoginResponse login(LoginDTO request) {
        try {
            User user = userRepository.findByEmail(request.getEmail());

            if(user == null) {
                throw new RuntimeException("Invalid Credentials");
            }

            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid Credentials");
            }

            String token = jwtUtils.generateToken(String.valueOf(user.getUserId()), user.getRole().name());

            return new LoginResponse(token, modelMapper.map(user, UserDTO.class));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication Error");
        }
    }
}
