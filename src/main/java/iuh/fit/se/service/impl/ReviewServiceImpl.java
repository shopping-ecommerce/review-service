package iuh.fit.se.service.impl;


import iuh.fit.se.dto.request.ReviewRequest;
import iuh.fit.se.dto.response.ApiResponse;
import iuh.fit.se.dto.response.FileClientResponse;
import iuh.fit.se.dto.response.ReviewResponse;
import iuh.fit.se.entity.Review;
import iuh.fit.se.mapper.ReviewMapper;
import iuh.fit.se.repository.ReviewRepository;
import iuh.fit.se.repository.httpClient.FileClient;
import iuh.fit.se.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    ReviewMapper reviewMapper;
    ReviewRepository reviewRepository;
    FileClient fileClient;
    @Override
    public ReviewResponse createReview(ReviewRequest request, List<MultipartFile> files) {
        Review review = reviewMapper.toReview(request);
        if (files != null && !files.isEmpty()) {
            FileClientResponse fileClientResponse = fileClient.uploadFile(files);
            review.setImages(fileClientResponse.getResult());
        }
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return  reviewRepository.findAll().stream().map(reviewMapper::toReviewResponse).toList();
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId).stream().map(reviewMapper::toReviewResponse).toList();
    }
}
