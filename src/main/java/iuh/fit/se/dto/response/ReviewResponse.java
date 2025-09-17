package iuh.fit.se.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    String productId; // Liên kết với Product
    String userId; // Người dùng để lại đánh giá
    String orderId; // Liên kết với đơn hàng để kiểm tra tính hợp lệ
    Integer rating; // Điểm đánh giá (1-5 sao)
    String comment; // Nội dung đánh giá
    List<String> images; // Hình ảnh kèm theo đánh giá
    Instant createdAt;
}
