package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();
}
