package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CategoryDTO;
import in.swarnavo.ecommerce_backend.dto.CategoryResponse;
import org.springframework.http.ProblemDetail;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO deleteCategory(Long categoryId);
}
