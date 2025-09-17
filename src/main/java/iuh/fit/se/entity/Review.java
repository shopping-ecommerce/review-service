package iuh.fit.se.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {
    @Id
    String id;

    @Indexed
    String productId; // Liên kết với Product

    @Indexed
    String userId; // Người dùng để lại đánh giá

    @Indexed
    String orderId; // Liên kết với đơn hàng để kiểm tra tính hợp lệ

    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5")
    Integer rating; // Điểm đánh giá (1-5 sao)

    String comment; // Nội dung đánh giá

    List<String> images; // Hình ảnh kèm theo đánh giá

    @CreatedDate
    @Builder.Default
    Instant createdAt = Instant.now();
}