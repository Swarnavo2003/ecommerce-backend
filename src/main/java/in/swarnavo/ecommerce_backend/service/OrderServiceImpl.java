package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.AddressDTO;
import in.swarnavo.ecommerce_backend.dto.OrderDTO;
import in.swarnavo.ecommerce_backend.dto.OrderItemDTO;
import in.swarnavo.ecommerce_backend.exception.BaseException;
import in.swarnavo.ecommerce_backend.exception.ResourceNotFoundException;
import in.swarnavo.ecommerce_backend.model.*;
import in.swarnavo.ecommerce_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final CartService cartService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        if (cart.getCartItems().isEmpty()) {
            throw new BaseException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Payment payment;
        if("COD".equalsIgnoreCase(paymentMethod)) {
            payment = new Payment("COD", cart.getTotalPrice(), "INR");
            payment.setAmount(cart.getTotalPrice());
            payment.setPgStatus("PENDING");
            payment.setPgName("Cash On Delivery");
        } else{
            payment = new Payment(
                    paymentMethod,
                    pgName,
                    pgPaymentId,
                    cart.getTotalPrice(),
                    "INR"
            );
            payment.setAmount(cart.getTotalPrice());
            payment.setCurrency("INR");
        }

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("CONFIRMED");
        order.setAddress(address);
        order.setPayment(payment);

        payment.setOrder(order);
        paymentRepository.save(payment);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            if(product.getQuantity() < quantity) {
                throw new BaseException("Insufficient stock for product: " + product.getProductName());
            }

            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
        });

        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        cartItems.forEach(item -> cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId()));

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(orderItem ->
                orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class))
        );
        orderDTO.setAddress(modelMapper.map(address, AddressDTO.class));

        return orderDTO;
    }
}
