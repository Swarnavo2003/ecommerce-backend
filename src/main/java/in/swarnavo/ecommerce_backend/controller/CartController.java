package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.CartDTO;
import in.swarnavo.ecommerce_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        CartDTO cart = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}
