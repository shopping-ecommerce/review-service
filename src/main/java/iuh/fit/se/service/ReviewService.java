package iuh.fit.se.service;

import iuh.fit.se.dto.request.ReviewRequest;
import iuh.fit.se.dto.response.ReviewResponse;
import iuh.fit.se.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewRequest request, List<MultipartFile> files);
    List<ReviewResponse> getAllReviews();
    List<ReviewResponse> getReviewsByProductId(String productId);
}
