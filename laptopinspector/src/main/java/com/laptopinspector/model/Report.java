package com.laptopinspector.model;

import java.time.LocalDateTime;

public class Report {

    private Long id;
    private String inspectionCycleId;
    private String reportType;
    private String fileName;
    private String filePath;
    private LocalDateTime createdAt;

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInspectionCycleId() {
        return inspectionCycleId;
    }

    public void setInspectionCycleId(
            String inspectionCycleId
    ) {
        this.inspectionCycleId = inspectionCycleId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(
            String reportType
    ) {
        this.reportType = reportType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(
            String fileName
    ) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(
            String filePath
    ) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }
}