package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.LoginDTO;
import in.swarnavo.ecommerce_backend.dto.LoginResponse;
import in.swarnavo.ecommerce_backend.dto.RegisterDTO;
import in.swarnavo.ecommerce_backend.dto.UserDTO;
import in.swarnavo.ecommerce_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Tag(name = "Auth APIs", description = "APIs for managing authentication and user creation")
    @Operation(summary = "Register User", description = "API to register users")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @Tag(name = "Auth APIs", description = "APIs for managing authentication and user creation")
    @Operation(summary = "Login User", description = "API to login users")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO request) {
        LoginResponse loginResponse = userService.login(request);
        if (loginResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
