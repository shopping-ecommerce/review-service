package iuh.fit.se.controller;

import iuh.fit.se.dto.request.ReviewRequest;
import iuh.fit.se.dto.response.ApiResponse;
import iuh.fit.se.dto.response.ReviewResponse;
import iuh.fit.se.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {
    ReviewService reviewService;
    @PostMapping(value = "/create",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    ApiResponse<ReviewResponse> createReview(@RequestPart("request") ReviewRequest request,
                                             @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Review created successfully")
                .result(reviewService.createReview(request,files))
                .build();
    }

    @GetMapping("/{productId}")
    ApiResponse<List<ReviewResponse>> getReviewsByProductId(@PathVariable("productId") String productId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(200)
                .message("Reviews retrieved successfully")
                .result(reviewService.getReviewsByProductId(productId))
                .build();
    }
}
