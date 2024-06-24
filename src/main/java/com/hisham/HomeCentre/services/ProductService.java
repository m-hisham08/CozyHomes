package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.payloads.products.ProductEditRequest;
import com.hisham.HomeCentre.payloads.products.ProductRequest;
import com.hisham.HomeCentre.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductService {
    Page<Product> getAllProducts(int page, int size, Sort.Direction sortDirection, String sortBy);
    Product getSingleProduct(Long productId);
    Product createProduct(CustomUserDetails userDetails, ProductRequest productRequest);
    Product editProduct(CustomUserDetails userDetails, Long productId, ProductEditRequest productEditRequest);
    void deleteProduct(CustomUserDetails userDetails, Long productId);
}
