package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.APIResponse;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.categories.CategoryRequest;
import com.hisham.HomeCentre.payloads.categories.CategoryResponse;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.security.CurrentUser;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.CategoryService;
import com.hisham.HomeCentre.services.RedisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class
CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        String cacheKey = AppConstants.Redis.CATEGORY_KEY_PREFIX + "all";

        List<CategoryResponse> cachedResponse = redisService.get(cacheKey, List.class);
        if (cachedResponse != null) {
            return ResponseEntity.ok(cachedResponse);
        }

        List<Category> categoryList = categoryService.getAllCategories();

        List<CategoryResponse> categoryResponse = new ArrayList<>();

        for(Category category: categoryList){
            User createdUser = userRepository.findById(category.getCreatedBy())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "ID", category.getCreatedBy()));
            UserSummary userSummaryCreatedBy
                    = new UserSummary(createdUser.getId(), createdUser.getFirstName(), createdUser.getLastName(), createdUser.getUsername());

            User modifiedUser = userRepository.findById(category.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", category.getCreatedBy()));
            UserSummary userSummaryModifiedBy
                    = new UserSummary(modifiedUser.getId(), modifiedUser.getFirstName(), modifiedUser.getLastName(), modifiedUser.getUsername());

            categoryResponse.add(
                    new CategoryResponse(
                            category.getId(),
                            category.getName(),
                            category.getCreatedAt(),
                            category.getLastModifiedAt(),
                            userSummaryCreatedBy,
                            userSummaryModifiedBy
                    )
            );
        }

        redisService.set(cacheKey, categoryResponse, 3600L);

        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId){
        String cacheKey = AppConstants.Redis.CATEGORY_KEY_PREFIX + categoryId;
        CategoryResponse cachedResponse = redisService.get(cacheKey, CategoryResponse.class);
        if(cachedResponse != null){
            return ResponseEntity.ok(cachedResponse);
        }

        Category category = categoryService.getCategory(categoryId);

        User createdByUser = userRepository.findById(category.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", category.getCreatedBy()));
        User modifiedByUser = userRepository.findById(category.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", category.getLastModifiedBy()));

        UserSummary createdByUserSummary = new UserSummary(
                createdByUser.getId(), createdByUser.getFirstName(), createdByUser.getLastName(), createdByUser.getUsername()
        );
        UserSummary modifiedByUserSummary = new UserSummary(
                modifiedByUser.getId(), modifiedByUser.getFirstName(), modifiedByUser.getLastName(), modifiedByUser.getUsername()
        );

        CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getName(), category.getCreatedAt(), category.getLastModifiedAt(), createdByUserSummary, modifiedByUserSummary);

        redisService.set(cacheKey, categoryResponse, AppConstants.Redis.TIME_TO_LIVE);

        return ResponseEntity.ok(categoryResponse);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> createCategory(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody @Valid CategoryRequest categoryRequest
            ){
        Category category = categoryService.createCategory(userDetails, categoryRequest.getName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{categoryId}")
                .buildAndExpand(category.getId())
                .toUri();

        return ResponseEntity.created(location).body(new APIResponse(true, "Category created successfully!"));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<APIResponse> updateCategory(
            @PathVariable Long categoryId,
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody @Valid CategoryRequest categoryRequest
    ){
        categoryService.editCategory(userDetails, categoryId, categoryRequest.getName());

        String cacheKey = AppConstants.Redis.CATEGORY_KEY_PREFIX + categoryId;
        redisService.delete(cacheKey);

        return ResponseEntity.ok(new APIResponse(Boolean.TRUE, "Category updated successfully!"));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ){
        categoryService.deleteCategory(categoryId);

        String cacheKey = AppConstants.Redis.CATEGORY_KEY_PREFIX + categoryId;
        redisService.delete(cacheKey);

        return ResponseEntity.noContent().build();
    }

}
