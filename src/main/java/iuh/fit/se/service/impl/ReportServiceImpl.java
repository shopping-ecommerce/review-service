package iuh.fit.se.service.impl;
import iuh.fit.event.dto.ProductInvalid;
import iuh.fit.se.dto.request.ReportRequest;
import iuh.fit.se.dto.request.ReportUpdateRequest;
import iuh.fit.se.dto.response.ReportResponse;
import iuh.fit.se.entity.Report;
import iuh.fit.se.entity.enums.ReportStatus;
import iuh.fit.se.mapper.ReportMapper;
import iuh.fit.se.repository.ReportRepository;
import iuh.fit.se.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public  class ReportServiceImpl implements ReportService {
    ReportMapper reportMapper;
    ReportRepository reportRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public ReportResponse createReport(ReportRequest request) {
        Report report= reportMapper.toReport(request);
        return reportMapper.toReportResponse(reportRepository.save(report));
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllByStatus(ReportStatus.PENDING);
    }

    @Override
    public List<ReportResponse> getReportsByProductId(String productId) {
        return reportRepository.findByProductId(productId);
    }

    @Override
    public ReportResponse updateReportStatus(ReportUpdateRequest request) {
        Report existingReport = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        existingReport.setStatus(ReportStatus.valueOf(request.getStatus()));
        Report savedReport = reportRepository.save(existingReport);

        // Send Kafka message if the status is RESOLVED
        if (ReportStatus.RESOLVED.equals(existingReport.getStatus())) {
            kafkaTemplate.send("product-invalid", ProductInvalid.builder()
                    .productId(request.getProductId())
                    .reason(request.getReason())
                    .build());
        }
        return reportMapper.toReportResponse(savedReport);
    }
}
