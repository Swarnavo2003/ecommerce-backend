package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
