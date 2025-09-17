package iuh.fit.se.mapper;

import iuh.fit.se.dto.request.ReportRequest;
import iuh.fit.se.dto.response.ReportResponse;
import iuh.fit.se.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toReport(ReportRequest request);
    ReportResponse toReportResponse(Report report);
}
