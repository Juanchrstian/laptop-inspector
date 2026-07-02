package backend.service;

import backend.entity.DeviceCheck;
import backend.entity.Report;
import backend.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PdfService pdfService;

    private final ReportRepository reportRepository;

    // =========================================
    // GENERATE REPORT
    // =========================================

    public void generateReport(
            DeviceCheck deviceCheck
    ) throws IOException {

        String cycleId =
                deviceCheck.getInspectionCycleId();

        String reportType =
                deviceCheck.getCheckType();

        // =====================================
        // PREVENT DUPLICATE REPORT
        // =====================================

        boolean exists =
                reportRepository
                        .existsByInspectionCycleIdAndReportType(
                                cycleId,
                                reportType
                        );

        if (exists) {
            return;
        }

        // =====================================
        // GENERATE PDF
        // =====================================

        String filePath =
                pdfService
                        .generateHandoverPdf(
                                deviceCheck
                        );

        // =====================================
        // SAVE REPORT
        // =====================================

        Report report =
                new Report();

        report.setInspectionCycleId(
                cycleId
        );

        report.setReportType(
                reportType
        );

        report.setFileName(
                Paths.get(filePath)
                        .getFileName()
                        .toString()
        );

        report.setFilePath(
                filePath
        );

        reportRepository.save(
                report
        );
    }
}