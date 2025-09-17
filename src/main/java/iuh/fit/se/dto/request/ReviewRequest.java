package iuh.fit.se.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRequest {
    String productId; // Liên kết với Product
    String userId; // Người dùng để lại đánh giá
    String orderId; // Liên kết với đơn hàng để kiểm tra tính hợp lệ
    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5")
    Integer rating; // Điểm đánh giá (1-5 sao)
    String comment; // Nội dung đánh giá
    List<String> images; // Hình ảnh kèm theo đánh giá
}
