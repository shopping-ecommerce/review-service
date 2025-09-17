package iuh.fit.se.entity;
import iuh.fit.se.entity.enums.ReportStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reports")
public class Report {
    @Id
    String id;

    @Indexed
    String productId; // Sản phẩm bị báo cáo

    @Indexed
    String userId; // Người báo cáo

    String reason; // Lý do báo cáo (ví dụ: sản phẩm giả, mô tả sai)

    @Builder.Default
    ReportStatus status = ReportStatus.PENDING; // Trạng thái báo cáo (PENDING, RESOLVED, REJECTED)

    @CreatedDate
            @Builder.Default
    Instant createdAt = Instant.now();
}