package in.swarnavo.ecommerce_backend.service;

import in.swarnavo.ecommerce_backend.dto.CategoryDTO;
import in.swarnavo.ecommerce_backend.exception.DuplicateResourceException;
import in.swarnavo.ecommerce_backend.model.Category;
import in.swarnavo.ecommerce_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creatng new category: {}", categoryDTO.getCategoryName());

        Category category = modelMapper.map(categoryDTO, Category.class);

        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(existingCategory != null) {
            throw new DuplicateResourceException("Category", "name", categoryDTO.getCategoryName());
        }

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully with ID: {}", savedCategory.getCategoryId());
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
