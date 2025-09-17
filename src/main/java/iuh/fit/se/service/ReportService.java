package iuh.fit.se.service;

import iuh.fit.se.dto.request.ReportRequest;
import iuh.fit.se.dto.request.ReportUpdateRequest;
import iuh.fit.se.dto.response.ReportResponse;

import java.util.List;

public interface ReportService {
    ReportResponse createReport(ReportRequest request);
    List<ReportResponse> getAllReports();
    List<ReportResponse> getReportsByProductId(String productId);
    ReportResponse updateReportStatus(ReportUpdateRequest request);
}
