package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Product;
import com.hisham.HomeCentre.models.Review;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.reviews.ReviewRequest;
import com.hisham.HomeCentre.repositories.ProductRepository;
import com.hisham.HomeCentre.repositories.ReviewRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ReviewServiceTests {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReviews() {
        Long productId = 1L;
        int page = 0;
        int size = 10;
        String sortDirection = "ASC";
        String sortBy = "comment";

        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);
        Page<Review> reviewPage = new PageImpl<>(List.of(new Review(), new Review()));
        when(reviewRepository.findByProduct(product, pageable)).thenReturn(reviewPage);

        Page<Review> result = reviewService.getAllReviews(productId, page, size, sortDirection, sortBy);

        assertEquals(2, result.getContent().size());
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).findByProduct(product, pageable);
    }

    @Test
    void testGetSingleReview() {
        Long reviewId = 1L;
        Review review = new Review();
        review.setId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review result = reviewService.getSingleReview(reviewId);

        assertNotNull(result);
        assertEquals(reviewId, result.getId());
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void testCreateReview() {
        Long productId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Great product!");
        reviewRequest.setRating(5F);

        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Review review = new Review();
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.createReview(productId, reviewRequest);

        assertNotNull(result);
        assertEquals("Great product!", result.getComment());
        assertEquals(5, result.getRating());
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L;
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user");

        Review review = new Review();
        review.setId(reviewId);
        review.setCreatedBy(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(userRepository.findById(review.getCreatedBy())).thenReturn(Optional.of(user));

        reviewService.deleteReview(userDetails, reviewId);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(userRepository, times(1)).findById(review.getCreatedBy());
        verify(reviewRepository, times(1)).delete(review);
    }
}
