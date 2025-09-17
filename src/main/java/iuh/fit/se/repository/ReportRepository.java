package iuh.fit.se.repository;

import iuh.fit.se.dto.response.ReportResponse;
import iuh.fit.se.entity.Report;
import iuh.fit.se.entity.enums.ReportStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    List<ReportResponse> findAllByStatus(ReportStatus status);
    List<ReportResponse> findByProductId(String productId);
}