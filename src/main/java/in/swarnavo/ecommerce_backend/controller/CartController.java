package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.dto.CartDTO;
import in.swarnavo.ecommerce_backend.dto.ProductDTO;
import in.swarnavo.ecommerce_backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Add Product To Cart", description = "API to add products to cart")
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        CartDTO cart = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Get All Carts", description = "API to get all carts")
    @GetMapping("/carts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOs, HttpStatus.FOUND);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Get User Carts", description = "API to get user cart")
    @GetMapping("/carts/user/cart")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<CartDTO> getUserCart() {
        CartDTO cartDTO = cartService.getUserCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Update Products In Cart", description = "API to update products in cart")
    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<CartDTO> updateCartProduct(
            @PathVariable Long productId,
            @PathVariable String operation
    ) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1: 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Remove Products From Cart", description = "API to remove products from carts")
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    @PreAuthorize("hasAnyRole('USER','SELLER','ADMIN')")
    public ResponseEntity<ProductDTO> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        ProductDTO product = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
