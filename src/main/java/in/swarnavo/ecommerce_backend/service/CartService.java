package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CartDTO;
import in.swarnavo.ecommerce_backend.dto.ProductDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getUserCart();

    CartDTO updateProductQuantityInCart(Long productId, int quantity);

    ProductDTO deleteProductFromCart(Long cartId, Long productId);
}
