package com.hisham.HomeCentre.services.impl;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.BadRequestException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.exceptions.CustomExceptions.UnauthorizedException;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.Review;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.reviews.ReviewRequest;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.repositories.ReviewRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Page<Review> getAllReviews(Long productId, int page, int size, String sortDirection, String sortBy) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > AppConstants.Defaults.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page number cannot exceed " + AppConstants.Defaults.MAX_PAGE_SIZE);
        }
        if (!sortDirection.equals("ASC") && !sortDirection.equals("DESC")) {
            throw new BadRequestException("Invalid sort direction!");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        Pageable pageable;
        if(sortDirection.equals("ASC")){
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);
        }
        else{
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortBy);
        }


        Page<Review> reviews = reviewRepository.findByProduct(product, pageable);

        return reviews;

    }

    @Override
    @Transactional
    public Review getSingleReview(Long reviewId) {
        if(reviewId <= 0){
            throw new ResourceNotFoundException("Review", "ID", reviewId);
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));

        return review;
    }

    @Override
    @Transactional
    public Review createReview(
            Long productId,
            ReviewRequest reviewRequest
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        Review review = Review.builder()
                .product(product)
                .comment(reviewRequest.getComment())
                .rating(reviewRequest.getRating())
                .build();

        product.addReview(review);

        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(UserDetails userDetails, Long reviewId) {
        if(reviewId <= 0){
            throw new BadRequestException("Invalid Review ID!");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));

        User user = userRepository.findById(review.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", review.getCreatedBy()));

        if(!userDetails.getUsername().equals(user.getUsername())){
            throw new UnauthorizedException("You do not have the access rights to modify this resource!");
        }

        reviewRepository.delete(review);
    }
}
