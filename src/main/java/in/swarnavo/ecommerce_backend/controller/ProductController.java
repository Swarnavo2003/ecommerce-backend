package in.swarnavo.ecommerce_backend.controller;

import in.swarnavo.ecommerce_backend.config.AppConstants;
import in.swarnavo.ecommerce_backend.dto.ProductDTO;
import in.swarnavo.ecommerce_backend.dto.ProductResponse;
import in.swarnavo.ecommerce_backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Create Product", description = "API to create product")
    @PostMapping("/seller/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId
    ) {
        ProductDTO product = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Get All Products", description = "API to fetch all products")
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Get Product By Category Id", description = "API to fetch products by category id")
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(
        @PathVariable Long categoryId,
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Get Products By Keyword", description = "API to fetch products by keyword")
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Update Product", description = "API to update product")
    @PutMapping("/seller/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long productId
    ) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Delete Product", description = "API to delete product")
    @DeleteMapping("/seller/products/{productId}")
    public ResponseEntity<ProductDTO> deletedProduct(
            @PathVariable Long productId
    ) {
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @Tag(name = "Product APIs", description = "APIs for managing products")
    @Operation(summary = "Fetch Seller Products", description = "API to fetch seller products")
    @GetMapping("/seller/products")
    public ResponseEntity<List<ProductDTO>> getSellerProducts() {
        List<ProductDTO> productDTOS = productService.getSellerProducts();
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }
}
