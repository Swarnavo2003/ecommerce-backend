package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.LoginDTO;
import in.swarnavo.ecommerce_backend.dto.LoginResponse;
import in.swarnavo.ecommerce_backend.dto.RegisterDTO;
import in.swarnavo.ecommerce_backend.dto.UserDTO;
import in.swarnavo.ecommerce_backend.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDTO request) {
        LoginResponse loginResponse = userService.login(request);
        if (loginResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
