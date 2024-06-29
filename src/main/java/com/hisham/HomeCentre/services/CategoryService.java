package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.security.CustomUserDetails;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategory(Long categoryId);
    Category createCategory(CustomUserDetails userDetails, String categoryName);
    void deleteCategory(Long categoryId);
    void editCategory(CustomUserDetails userDetails, Long categoryId, String newCategoryName);
}
