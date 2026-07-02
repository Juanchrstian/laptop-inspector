package backend.repository;

import backend.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository
        extends JpaRepository<Report, Long> {

    // =========================================
    // FIND BY INSPECTION CYCLE
    // =========================================

    List<Report> findByInspectionCycleId(
            String inspectionCycleId
    );

    // =========================================
    // FIND BY REPORT TYPE
    // =========================================

    List<Report> findByReportType(
            String reportType
    );

    // =========================================
    // FIND SPECIFIC REPORT
    // =========================================

    Optional<Report>
    findByInspectionCycleIdAndReportType(
            String inspectionCycleId,
            String reportType
    );

    // =========================================
    // VALIDATION
    // =========================================

    boolean existsByInspectionCycleIdAndReportType(
            String inspectionCycleId,
            String reportType
    );

    // =========================================
    // RECENT REPORTS
    // =========================================

    List<Report>
    findTop10ByOrderByIdDesc();
}