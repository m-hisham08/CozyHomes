package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.BadRequestException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ConflictException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.products.ProductEditRequest;
import com.hisham.HomeCentre.payloads.products.ProductRequest;
import com.hisham.HomeCentre.repositories.CategoryRepository;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.ProductService;
import com.hisham.HomeCentre.services.RedisService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private RedisService redisService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Page<Product> getAllProducts(int page, int size, Sort.Direction sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);
        Page<Product> products = productRepository.findAll(pageable);
        return products;
    }

    @Override
    @Transactional
    public Product getSingleProduct(Long productId) {
        if (productId <= 0) {
            throw new BadRequestException("Invalid product ID");
        }

//        String cacheKey = AppConstants.Redis.PRODUCT_KEY_PREFIX + productId;
//        Product cachedProduct = redisService.get(cacheKey, Product.class);
//        if (cachedProduct != null) {
//            return cachedProduct;
//        }

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "ID", productId)
        );

        if (product.getCategory() == null) {
            throw new ResourceNotFoundException("Category", "ID", "not found");
        }

//        redisService.set(cacheKey, product, AppConstants.Redis.TIME_TO_LIVE);
        return product;
    }

    @Override
    @Transactional
    public Product createProduct(CustomUserDetails userDetails, ProductRequest productRequest) {
        Boolean isExists = productRepository.existsByName(productRequest.getName());
        if (isExists) {
            throw new ConflictException("The Product with the product name " +
                    productRequest.getName() + " already exists!");
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", productRequest.getCategoryId()));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setCategory(category);
        product.setDescription(productRequest.getDescription());
        product.setImageURL(productRequest.getImageURL());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product editProduct(CustomUserDetails userDetails, Long productId, ProductEditRequest productEditRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        if (productEditRequest.getName() != null) {
            product.setName(productEditRequest.getName());
        }
        if (productEditRequest.getStock() != null) {
            product.setStock(productEditRequest.getStock());
        }
        if (productEditRequest.getDescription() != null) {
            product.setDescription(productEditRequest.getDescription());
        }
        if (productEditRequest.getImageURL() != null) {
            product.setImageURL(productEditRequest.getImageURL());
        }
        if (productEditRequest.getPrice() != null) {
            product.setPrice(productEditRequest.getPrice());
        }
        if (productEditRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(productEditRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", productEditRequest.getCategoryId()));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(CustomUserDetails userDetails, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        productRepository.delete(product);
    }
}
