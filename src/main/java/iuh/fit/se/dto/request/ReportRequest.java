package iuh.fit.se.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportRequest {
    String productId; // Sản phẩm bị báo cáo
    String userId; // Người báo cáo
    String reason; // Lý do báo cáo (ví dụ: sản phẩm giả, mô tả sai)
}
