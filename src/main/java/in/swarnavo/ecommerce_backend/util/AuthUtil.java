package in.swarnavo.ecommerce_backend.util;

import in.swarnavo.ecommerce_backend.exception.ResourceNotFoundException;
import in.swarnavo.ecommerce_backend.model.User;
import in.swarnavo.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return authentication.getPrincipal().toString();
    }

    public String loggedInEmail() {
        String userId = loggedInUserId();

        if(userId == null) {
            return null;
        }

        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId : " + userId));
        return user != null ? user.getEmail() : null;
    }

    public User loggedInUser() {
        String userId = loggedInUserId();

        if (userId == null) {
            return null;
        }

        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId : " + userId));
    }
}
