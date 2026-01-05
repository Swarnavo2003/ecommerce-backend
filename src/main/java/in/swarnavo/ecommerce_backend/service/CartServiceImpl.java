package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CartDTO;
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

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()) {
            throw new BaseException("No carts exists");
        }

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(cartItem -> {
                                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                                productDTO.setQuantity(cartItem.getQuantity());
                                return productDTO;
                            })
                            .toList();

                    cartDTO.setProducts(products);

                    return cartDTO;
                })
                .toList();

        return cartDTOS;
    }

    @Override
    public CartDTO getUserCart() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .toList();

        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, int quantity) {
        // STEP 1: Get the logged-in user's cart
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);

        // STEP 2: Validate cart exists (double-check with findById)
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // STEP 3: Find the product in database
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // STEP 4: Validate product availability (only for INCREASE)
        if(product.getQuantity() == 0) {
            throw new BaseException(product.getProductName() + " is not availabke");
        }

        if(product.getQuantity() < quantity) {
            throw new BaseException("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity());
        }

        // STEP 5: Find the CartItem (the product in THIS specific cart)
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new BaseException("Product " + product.getProductName() + " not available in the cart");
        }

        // STEP 6: Calculate the new quantity after update
        int newQuantity = cartItem.getQuantity() + quantity;

        // STEP 7: Validate new quantity is not negative
        if (newQuantity < 0) {
            throw new BaseException("The resulting quantity cannot be negative");
        }

        // STEP 8: Handle based on new quantity value
        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {

            double priceChange = product.getSpecialPrice() * quantity;

            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(newQuantity);
            cartItem.setDiscount(product.getDiscount());

            cart.setTotalPrice(cart.getTotalPrice() + priceChange);

            // cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        // STEP 9: Refresh cart to get updated state
        cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // STEP 10: Convert to DTO and return
        return mapCartToDTO(cart);
    }


    @Transactional
    @Override
    public ProductDTO deleteProductFromCart(Long cartId, Long productId) {
        // STEP 1: Find the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // STEP 2: Find the cart item to delete
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product not found in cart");
        }

        Product product = cartItem.getProduct();
        int deletedQuantity = cartItem.getQuantity();

        // STEP 3: Calculate price to subtract from cart total
        double priceToRemove = cartItem.getProductPrice() * cartItem.getQuantity();

        // STEP 4: Update cart total price
        cart.setTotalPrice(cart.getTotalPrice() - priceToRemove);

        // STEP 5: Delete the cart item
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        // STEP 6: Save updated cart
        // cartRepository.save(cart);
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setQuantity(deletedQuantity);
        return productDTO;
    }

    // Map CartItems to ProductDTOs
    private CartDTO mapCartToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(cartItem -> {
                    ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(cartItem.getQuantity());
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
