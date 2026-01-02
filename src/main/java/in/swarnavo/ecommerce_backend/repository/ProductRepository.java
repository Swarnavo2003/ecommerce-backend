package in.swarnavo.ecommerce_backend.repository;

import in.swarnavo.ecommerce_backend.model.Category;
import in.swarnavo.ecommerce_backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String s, Pageable pageDetails);

    @Query("SELECT p FROM Product p WHERE p.user.userId = :userId")
    List<Product> findByUserId(@Param("userId") Long userId);
}
