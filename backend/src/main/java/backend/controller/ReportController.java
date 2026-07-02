package backend.controller;

import backend.entity.Report;
import backend.repository.ReportRepository;

import backend.entity.DeviceCheck;
import backend.repository.DeviceCheckRepository;
import backend.service.PdfService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final DeviceCheckRepository deviceCheckRepository;
    private final PdfService pdfService;

    // =========================================
    // GET ALL REPORTS
    // =========================================

    @GetMapping
    public List<Report> getAllReports() {

        return reportRepository.findAll();
    }

    // =========================================
    // GET REPORT BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(
            @PathVariable Long id
    ) {

        return reportRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    // =========================================
    // GET REPORT BY CYCLE ID
    // =========================================

    @GetMapping("/cycle/{inspectionCycleId}")
    public List<Report> getReportByCycleId(
            @PathVariable String inspectionCycleId
    ) {

        return reportRepository
                .findByInspectionCycleId(
                        inspectionCycleId
                );
    }

    // =========================================
    // DOWNLOAD PDF
    // =========================================

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadReport(
            @PathVariable Long id
    ) throws MalformedURLException {

        Report report =
                reportRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found."
                                )
                        );

        Path path =
                Paths.get(
                        report.getFilePath()
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {

            throw new RuntimeException(
                    "PDF file not found."
            );
        }

        return ResponseEntity.ok()

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + report.getFileName()
                                + "\""
                )

                .body(resource);
    }

    // =========================================
    // GET LATEST REPORTS
    // =========================================

    @GetMapping("/recent")
    public List<Report> getRecentReports() {

        return reportRepository
                .findTop10ByOrderByIdDesc();
    }

    // =========================================
    // DOWNLOAD REPORT BY CYCLE ID
    // =========================================

    @GetMapping("/cycle/{inspectionCycleId}/download")
    public ResponseEntity<Resource>
    downloadLatestReportByCycleId(
            @PathVariable
            String inspectionCycleId
    ) throws MalformedURLException {

        List<Report> reports =
                reportRepository
                        .findByInspectionCycleId(
                                inspectionCycleId
                        );

        if (reports.isEmpty()) {

            throw new RuntimeException(
                    "Report not found."
            );
        }

        Report latest =
                reports.get(
                        reports.size() - 1
                );

        Path path =
                Paths.get(
                        latest.getFilePath()
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {

            throw new RuntimeException(
                    "PDF file not found."
            );
        }

        return ResponseEntity.ok()

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + latest.getFileName()
                                + "\""
                )

                .body(resource);
    }

    // =========================================
    // DOWNLOAD COMPARISON REPORT
    // =========================================

    @GetMapping(
            "/compare/{inspectionCycleId}"
    )
    public ResponseEntity<Resource>
    downloadComparisonReport(
            @PathVariable
            String inspectionCycleId
    ) throws Exception {

        List<DeviceCheck> checks =
                deviceCheckRepository
                        .findByInspectionCycleIdOrderByCheckTimeAsc(
                                inspectionCycleId
                        );

        if (checks.isEmpty()) {

            throw new RuntimeException(
                    "Inspection data not found."
            );
        }

        DeviceCheck before = null;
        DeviceCheck after = null;

        for (DeviceCheck item : checks) {

            if ("BEFORE".equalsIgnoreCase(
                    item.getCheckType()
            )) {

                before = item;
            }

            if ("AFTER".equalsIgnoreCase(
                    item.getCheckType()
            )) {

                after = item;
            }
        }

        if (before == null
                || after == null) {

            throw new RuntimeException(
                    "Comparison data incomplete."
            );
        }

        String filePath =
                pdfService
                        .generateComparisonPdf(
                                before,
                                after
                        );

        Path path =
                Paths.get(
                        filePath
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {

            throw new RuntimeException(
                    "PDF file not found."
            );
        }

        return ResponseEntity.ok()

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + path.getFileName()
                                + "\""
                )

                .body(resource);
    }
}