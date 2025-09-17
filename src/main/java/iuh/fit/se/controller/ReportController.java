package iuh.fit.se.controller;

import iuh.fit.se.dto.request.ReportRequest;
import iuh.fit.se.dto.request.ReportUpdateRequest;
import iuh.fit.se.dto.response.ApiResponse;
import iuh.fit.se.dto.response.ReportResponse;
import iuh.fit.se.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/report")
public class ReportController {
    ReportService reportService;
    @PostMapping("/create")
    ApiResponse<ReportResponse> createReport(@RequestBody ReportRequest request){
        return ApiResponse.<ReportResponse>builder()
                .code(200)
                .message("Report created successfully")
                .result(reportService.createReport(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/searchByStatusPending")
    ApiResponse<List<ReportResponse>> searchByStatusPending(){
        return ApiResponse.<List<ReportResponse>>builder()
                .code(200)
                .message("Report retrieved successfully")
                .result(reportService.getAllReports())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateStatus")
    ApiResponse<ReportResponse> updateStatus(@RequestBody ReportUpdateRequest request){
        return ApiResponse.<ReportResponse>builder()
                .code(200)
                .message("Report status updated successfully")
                .result(reportService.updateReportStatus(request))
                .build();
    }
}
