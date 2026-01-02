package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.ProductDTO;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, @Valid ProductDTO productDTO);
}
