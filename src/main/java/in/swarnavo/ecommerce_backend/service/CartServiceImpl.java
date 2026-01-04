package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CartDTO;
import in.swarnavo.ecommerce_backend.dto.CartItemDTO;
import in.swarnavo.ecommerce_backend.dto.ProductDTO;
import in.swarnavo.ecommerce_backend.exception.BaseException;
import in.swarnavo.ecommerce_backend.exception.DuplicateResourceException;
import in.swarnavo.ecommerce_backend.exception.ResourceNotFoundException;
import in.swarnavo.ecommerce_backend.model.Cart;
import in.swarnavo.ecommerce_backend.model.CartItem;
import in.swarnavo.ecommerce_backend.model.Product;
import in.swarnavo.ecommerce_backend.repository.CartItemRepository;
import in.swarnavo.ecommerce_backend.repository.CartRepository;
import in.swarnavo.ecommerce_backend.repository.ProductRepository;
import in.swarnavo.ecommerce_backend.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final AuthUtil authUtil;

    @Override
    @Transactional
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Step 1: Get or create user's cart
        Cart cart = createCart();

        // Step 2: Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with productId" + productId));

        // Step 3: Check if product already exists in cart
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem != null) {
            throw new DuplicateResourceException("Product already exists in the cart");
        }

        // Step 4: Validate product availability
        if(product.getQuantity() == 0) {
            throw new ResourceNotFoundException(product.getProductName() + "is not available");
        }

        if(product.getQuantity() < quantity) {
            throw new BaseException("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity());
        }

        // Step 5: Create new cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        cart.getCartItems().add(newCartItem);

         // product.setQuantity(product.getQuantity() - quantity);
         // productRepository.save(product);

        // Step 6: Update cart total price
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cart = cartRepository.save(cart);

        // Step 7: Convert to DTO and return
        return mapCartToDTO(cart);
    }

    // Map CartItems to ProductDTOs
    private CartDTO mapCartToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(cartItem -> {
                    ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                    return productDTO;
                })
                .toList();

        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
