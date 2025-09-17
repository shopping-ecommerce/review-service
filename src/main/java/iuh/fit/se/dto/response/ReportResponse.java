package iuh.fit.se.dto.response;

import iuh.fit.se.entity.enums.ReportStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;


import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    String id;
    String productId; // Sản phẩm bị báo cáo
    String userId; // Người báo cáo

    String reason; // Lý do báo cáo (ví dụ: sản phẩm giả, mô tả sai)

    ReportStatus status; // Trạng thái báo cáo (PENDING, RESOLVED, REJECTED)
    Instant createdAt;
}
