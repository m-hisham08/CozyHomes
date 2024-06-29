package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.BadRequestException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.APIResponse;
import com.hisham.HomeCentre.payloads.PagedResponse;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.categories.CategoryResponse;
import com.hisham.HomeCentre.payloads.products.ProductEditRequest;
import com.hisham.HomeCentre.payloads.products.ProductRequest;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.security.CurrentUser;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.CategoryService;
import com.hisham.HomeCentre.services.ProductService;
import com.hisham.HomeCentre.services.RedisService;
import com.hisham.HomeCentre.services.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @GetMapping("")
    public ResponseEntity<PagedResponse<ProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = AppConstants.Defaults.PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.Defaults.PAGE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Defaults.SORT_BY) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.Defaults.SORT) String sortDirection
    ) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.Defaults.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page number cannot exceed " + AppConstants.Defaults.MAX_PAGE_SIZE);
        }
        if (!sortDirection.equals("ASC") && !sortDirection.equals("DESC")) {
            throw new BadRequestException("Invalid sort direction!");
        }

        String cacheKey = String.format("products::page:%d::size:%d::sortBy:%s::sortDirection:%s", page, size, sortBy, sortDirection);
        PagedResponse<ProductResponse> cachedResponse = redisService.get(cacheKey, PagedResponse.class);
        if (cachedResponse != null) {
            return ResponseEntity.ok(cachedResponse);
        }

        Page<Product> pagedProduct;
        if (sortDirection.equals("ASC")) {
            pagedProduct = productService.getAllProducts(page, size, Sort.Direction.ASC, sortBy);
        } else {
            pagedProduct = productService.getAllProducts(page, size, Sort.Direction.DESC, sortBy);
        }

        List<Product> products = pagedProduct.getContent();
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : products) {
            Category category = categoryService.getCategory(product.getCategory().getId());
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName()).build();

            User user = userRepository.findById(product.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", product.getCreatedBy()));
            UserSummary userSummary = UserSummary.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setImageURL(product.getImageURL());
            productResponse.setStock(product.getStock());
            productResponse.setDescription(product.getDescription());
            productResponse.setCreatedAt(product.getCreatedAt());
            productResponse.setLastModifiedAt(product.getLastModifiedAt());
            productResponse.setCategory(categoryResponse);
            productResponse.setCreatedBy(userSummary);

            productResponseList.add(productResponse);
        }

        PagedResponse<ProductResponse> response = new PagedResponse<>(
                productResponseList,
                pagedProduct.getNumber(),
                pagedProduct.getSize(),
                pagedProduct.getNumberOfElements(),
                pagedProduct.getTotalPages(),
                pagedProduct.isLast()
        );

        redisService.set(cacheKey, response, AppConstants.Redis.TIME_TO_LIVE);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "productId") Long productId){

        String cacheKey = AppConstants.Redis.PRODUCT_KEY_PREFIX + productId;
        ProductResponse cachedProduct = redisService.get(cacheKey, ProductResponse.class);
        if (cachedProduct != null) {
            return ResponseEntity.ok(cachedProduct);
        }

        Product product = productService.getSingleProduct(productId);

        Category category = categoryService.getCategory(product.getCategory().getId());
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName()).build();

        User user = userRepository.findById(product.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", product.getCreatedBy()));
        UserSummary userSummary = UserSummary.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setImageURL(product.getImageURL());
        productResponse.setStock(product.getStock());
        productResponse.setDescription(product.getDescription());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setLastModifiedAt(product.getLastModifiedAt());
        productResponse.setCategory(categoryResponse);
        productResponse.setCreatedBy(userSummary);

        redisService.set(cacheKey, productResponse, AppConstants.Redis.TIME_TO_LIVE);

        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> createProduct(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody @Valid ProductRequest productRequest
    ){
        Product product = productService.createProduct(userDetails, productRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(new APIResponse(Boolean.TRUE, "Product created successfully!"));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable(name = "productId") Long productId
    ){
        if(productId < 0){
            throw new BadRequestException("Invalid product ID!");
        }

        Product product = productService.getSingleProduct(productId);
        productService.deleteProduct(userDetails, product.getId());

        String cacheKey = AppConstants.Redis.PRODUCT_KEY_PREFIX + productId;
        redisService.delete(cacheKey);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> editProduct(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long productId,
            @RequestBody @Valid ProductEditRequest productEditRequest
    )
    {
        if(productId < 0){
            throw new BadRequestException("Invalid product ID!");
        }

       Product product = productService.getSingleProduct(productId);
       Product editedProduct = productService.editProduct(userDetails, product.getId(), productEditRequest);

       Category category = categoryService.getCategory(editedProduct.getCategory().getId());
       CategoryResponse categoryResponse = CategoryResponse.builder()
               .id(category.getId())
               .name(category.getName())
               .build();

       User user = userRepository.findById(editedProduct.getCreatedBy())
               .orElseThrow(() -> new ResourceNotFoundException("User", "ID", editedProduct.getCreatedBy()));
       UserSummary userSummary = new UserSummary(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());

        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(categoryResponse)
                .description(product.getDescription())
                .imageURL(product.getImageURL())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .createdBy(userSummary)
                .isAvailable(product.getStock() > 0)
                .build();

        String cacheKey = AppConstants.Redis.PRODUCT_KEY_PREFIX + productId;
        redisService.set(cacheKey, productResponse, AppConstants.Redis.TIME_TO_LIVE);

       return ResponseEntity.ok().body(productResponse);
    }
}
