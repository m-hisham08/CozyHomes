package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.constants.AppConstants;
import com.hisham.HomeCentre.exceptions.CustomExceptions.ResourceNotFoundException;
import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.Review;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.PagedResponse;
import com.hisham.HomeCentre.payloads.UserSummary;
import com.hisham.HomeCentre.payloads.categories.CategoryResponse;
import com.hisham.HomeCentre.payloads.products.ProductResponse;
import com.hisham.HomeCentre.payloads.reviews.ReviewRequest;
import com.hisham.HomeCentre.payloads.reviews.ReviewResponse;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.security.CurrentUser;
import com.hisham.HomeCentre.security.CustomUserDetails;
import com.hisham.HomeCentre.services.ProductService;
import com.hisham.HomeCentre.services.RedisService;
import com.hisham.HomeCentre.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<PagedResponse<ReviewResponse>> getAllReviews(
            @PathVariable(name = "productId") Long productId,
            @RequestParam(name = "page", defaultValue = AppConstants.Defaults.PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.Defaults.PAGE_SIZE) int size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.Defaults.SORT_BY) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.Defaults.SORT) String sortDirection
    )
    {
        String cacheKey = String.format("reviews::page:%d::size:%d::sortBy:%s::sortDirection:%s", page, size, sortBy, sortDirection);
        PagedResponse<ReviewResponse> cachedResponse = redisService.get(cacheKey, PagedResponse.class);
        if (cachedResponse != null) {
            return ResponseEntity.ok(cachedResponse);
        }

        Page<Review> pagedReviews = reviewService.getAllReviews(productId, page, size, sortDirection, sortBy);

        List<Review> reviews = pagedReviews.getContent();
        List<ReviewResponse> reviewResponseList = new ArrayList<>();
        for(Review review: reviews){
            User user = userRepository.findById(review.getCreatedBy())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "ID", review.getCreatedBy()));
            UserSummary userSummary = new UserSummary(
                    user.getId(), user.getFirstName(), user.getLastName(), user.getUsername()
            );
            reviewResponseList.add(ReviewResponse.builder()
                            .id(review.getId())
                            .rating(review.getRating())
                            .comment(review.getComment())
                            .createdAt(review.getCreatedAt())
                            .createdBy(userSummary)
                    .build());
        }

        PagedResponse<ReviewResponse> response = new PagedResponse<ReviewResponse>(
                reviewResponseList, pagedReviews.getNumber(), pagedReviews.getSize(), pagedReviews.getNumberOfElements(), pagedReviews.getTotalPages(), pagedReviews.isLast()
        );
        redisService.set(cacheKey, response, AppConstants.Redis.TIME_TO_LIVE);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable(name = "reviewId") Long reviewId
    ){
        String cacheKey = AppConstants.Redis.REVIEW_KEY_PREFIX + reviewId;
        ReviewResponse cachedResponse = redisService.get(cacheKey,ReviewResponse.class);
        if(cachedResponse != null){
                return ResponseEntity.ok(cachedResponse);
        }

        Review review = reviewService.getSingleReview(reviewId);

        User user = userRepository.findById(review.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", review.getCreatedBy()));
        UserSummary userSummary = new UserSummary(
                user.getId(), user.getFirstName(), user.getLastName(), user.getUsername()
        );

        Product product = review.getProduct();

        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .imageURL(product.getImageURL())
                .category(CategoryResponse.builder().id(product.getCategory().getId()).name( product.getCategory().getName()).build())
                .stock(product.getStock())
                .price(product.getPrice())
                .description(product.getDescription())
                .isAvailable(product.isAvailable())
                .build();


        ReviewResponse reviewResponse = ReviewResponse.builder()
                .id(review.getId())
                .product(productResponse)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .createdBy(userSummary)
                .build();

        redisService.set(cacheKey, reviewResponse, AppConstants.Redis.TIME_TO_LIVE);

        return ResponseEntity.ok(reviewResponse);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody @Valid ReviewRequest reviewRequest,
            @PathVariable(name = "productId") Long productId
    )
    {
        Review review = reviewService.createReview(productId, reviewRequest);

        User user = userRepository.findById(review.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", review.getCreatedBy()));
        UserSummary userSummary = new UserSummary(
                user.getId(), user.getFirstName(), user.getLastName(), user.getUsername()
        );

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .id(review.getId())
                .product(ProductResponse.builder().id(review.getProduct().getId()).name(review.getProduct().getName()).build())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .createdBy(userSummary)
                .build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/reviewId")
                .buildAndExpand(review.getId())
                .toUri();

        return ResponseEntity.created(location).body(reviewResponse);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteProduct(
            @CurrentUser UserDetails userDetails,
            @PathVariable Long reviewId
    )
    {
        reviewService.deleteReview(userDetails, reviewId);

        String cacheKey = AppConstants.Redis.REVIEW_KEY_PREFIX + reviewId;
        redisService.delete(cacheKey);
        
        return ResponseEntity.noContent().build();
    }

}
