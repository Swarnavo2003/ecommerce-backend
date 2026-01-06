package in.swarnavo.ecommerce_backend.repository;

import in.swarnavo.ecommerce_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
