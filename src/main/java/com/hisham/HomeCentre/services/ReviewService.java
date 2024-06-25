package com.hisham.HomeCentre.services;

import com.hisham.HomeCentre.models.Review;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.reviews.ReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

public interface ReviewService {
    Page<Review> getAllReviews(Long productId, int page, int size, String sortDirection, String sortBy);
    Review getSingleReview(Long reviewId);
    Review createReview(Long productId, ReviewRequest reviewRequest);
    void deleteReview(UserDetails userDetails, Long reviewId);
}
