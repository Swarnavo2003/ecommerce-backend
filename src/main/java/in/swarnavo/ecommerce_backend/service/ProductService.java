package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.ProductDTO;
import in.swarnavo.ecommerce_backend.dto.ProductResponse;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, @Valid ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
