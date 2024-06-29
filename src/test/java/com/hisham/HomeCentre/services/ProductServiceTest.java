package com.hisham.HomeCentre.services;


import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.products.ProductEditRequest;
import com.hisham.HomeCentre.payloads.products.ProductRequest;
import com.hisham.HomeCentre.repositories.CategoryRepository;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        int page = 0;
        int size = 10;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortBy = "name";

        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
        Page<Product> productPage = new PageImpl<>(List.of(new Product(), new Product()));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<Product> result = productService.getAllProducts(page, size, sortDirection, sortBy);

        assertEquals(2, result.getContent().size());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetSingleProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        Category category = new Category();
        product.setCategory(category);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getSingleProduct(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        CustomUserDetails userDetails = new CustomUserDetails();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("New Product");
        productRequest.setCategoryId(1L);
        productRequest.setDescription("Product Description");
        productRequest.setImageURL("http://image.url");
        productRequest.setPrice(BigDecimal.valueOf(100));
        productRequest.setStock(10L);

        Category category = new Category();
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.existsByName(productRequest.getName())).thenReturn(false);

        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(userDetails, productRequest);

        assertNotNull(result);
        verify(categoryRepository, times(1)).findById(productRequest.getCategoryId());
        verify(productRepository, times(1)).existsByName(productRequest.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testEditProduct() {
        CustomUserDetails userDetails = new CustomUserDetails();
        Long productId = 1L;
        ProductEditRequest productEditRequest = new ProductEditRequest();
        productEditRequest.setName("Edited Product");
        productEditRequest.setStock(20L);
        productEditRequest.setDescription("Updated Description");
        productEditRequest.setImageURL("http://new.image.url");
        productEditRequest.setPrice(BigDecimal.valueOf(150));
        productEditRequest.setCategoryId(1L);

        Product product = new Product();
        Category category = new Category();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(productEditRequest.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.editProduct(userDetails, productId, productEditRequest);

        assertNotNull(result);
        assertEquals("Edited Product", product.getName());
        assertEquals(20, product.getStock());
        assertEquals("Updated Description", product.getDescription());
        assertEquals("http://new.image.url", product.getImageURL());
        assertEquals(BigDecimal.valueOf(150), product.getPrice());
        assertEquals(category, product.getCategory());
        verify(productRepository, times(1)).findById(productId);
        verify(categoryRepository, times(1)).findById(productEditRequest.getCategoryId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        CustomUserDetails userDetails = new CustomUserDetails();
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(userDetails, productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
    }
}
