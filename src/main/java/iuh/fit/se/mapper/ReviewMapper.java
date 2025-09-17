package iuh.fit.se.mapper;

import iuh.fit.se.dto.request.ReviewRequest;
import iuh.fit.se.dto.response.ReviewResponse;
import iuh.fit.se.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewResponse toReviewResponse(Review review);
    Review toReview(ReviewRequest request);
}
