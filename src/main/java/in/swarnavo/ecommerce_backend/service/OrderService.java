package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.OrderDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
