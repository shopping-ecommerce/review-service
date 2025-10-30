package iuh.fit.se.service.impl;


import iuh.fit.se.dto.request.DeleteRequest;
import iuh.fit.se.dto.request.ReviewRequest;
import iuh.fit.se.dto.response.FileClientResponse;
import iuh.fit.se.dto.response.ReviewResponse;
import iuh.fit.se.entity.Review;
import iuh.fit.se.exception.AppException;
import iuh.fit.se.exception.ErrorCode;
import iuh.fit.se.mapper.ReviewMapper;
import iuh.fit.se.repository.ReviewRepository;
import iuh.fit.se.repository.httpClient.FileClient;
import iuh.fit.se.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public void deleteByProductId(String productId) {
        Instant t0 = Instant.now();
        List<Review> reviews = reviewRepository.findByProductId(productId);
        int count = reviews == null ? 0 : reviews.size();
        log.info("[Review] deleteByProductId productId={} count={}", productId, count);
        if (count == 0) return;
        List<String> urls = reviews.stream()
                .filter(Objects::nonNull)
                .map(Review::getImages)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(u -> u != null && !u.isBlank())
                .distinct()
                .collect(Collectors.toList());

        if (!urls.isEmpty()) {
            log.info("[Review]   deleting images count={}", urls.size());
            try {
                fileClient.deleteByUrl(DeleteRequest.builder().urls(urls).build());
                log.info("[Review]   ✓ images deleted");
            } catch (Exception e) {
                log.warn("[Review]   ⚠ delete images failed: {}", e);
            }
        } else {
            log.info("[Review]   no images to delete");
        }

        reviewRepository.deleteAll(reviews);
        log.info("[Review]   ✓ reviews deleted productId={} timeMs={}",
                productId, Duration.between(t0, Instant.now()).toMillis());
    }
}
