package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.exceptions.CustomExceptions.BadRequestException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ConflictException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Category;
import com.hisham.HomeCentre.repositories.CategoryRepository;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category getCategory(Long categoryId) {
        if(categoryId <= 0){
            throw new BadRequestException("Invalid category ID: " + categoryId);
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () ->  new ResourceNotFoundException("Category", "CategoryID", categoryId)
        );

        return category;
    }

    @Override
    @Transactional
    public Category createCategory(CustomUserDetails userDetails, String categoryName) {
        if(!StringUtils.hasText(categoryName) || categoryName == null){
            throw new BadRequestException("Please provide a category name!");
        }

        if(categoryRepository.existsByName(categoryName)){
            throw new ConflictException("The category already exists!");
        }

        Category category = new Category();
        category.setName(categoryName);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if(categoryId <= 0){
            throw new BadRequestException("Please provide a valid category ID");
        }

        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category", "ID", categoryId);
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catgory", "ID", categoryId));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void editCategory(CustomUserDetails userDetails, Long categoryId, String newCategoryName) {
        if(!StringUtils.hasText(newCategoryName) || newCategoryName == null){
            throw new BadRequestException("Please provide a category name!");
        }

        if(categoryRepository.existsByName(newCategoryName)){
            throw new ConflictException("The category already exists!");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "ID", categoryId));

        category.setName(newCategoryName);

        categoryRepository.save(category);
    }
}
