package com.laptopinspector.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeviceCheck {

    // =========================================
    // DATABASE
    // =========================================

    private Long id;

    // =========================================
    // INSPECTION CYCLE
    // =========================================

    private String inspectionCycleId;

    // =========================================
    // DEVICE INFO
    // =========================================

    private String deviceId;

    private String serialNumber;

    private String username;

    private String borrowerName;

    private String inspector;

    // =========================================
    // SYSTEM
    // =========================================

    private String systemName;

    private String systemModel;

    private String cpu;

    private String gpu;

    // =========================================
    // HARDWARE
    // =========================================

    private Long totalRam;

    private Long totalStorage;

    private Long availableStorage;

    // =========================================
    // OS
    // =========================================

    private String os;

    // =========================================
    // INSPECTION
    // =========================================

    private String checkType;

    private Double batteryHealth;

    private Boolean batteryPresent;

    // =========================================
    // TIMESTAMP
    // =========================================

    private LocalDateTime scanStartedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime checkTime;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public DeviceCheck() {
    }

    // =========================================
    // GETTER & SETTER
    // =========================================

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

    public String getDeviceId() {

        return deviceId;
    }

    public void setDeviceId(String deviceId) {

        this.deviceId = deviceId;
    }

    public String getSerialNumber() {

        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {

        this.serialNumber = serialNumber;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getBorrowerName() {

        return borrowerName;
    }

    public void setBorrowerName(
            String borrowerName
    ) {

        this.borrowerName = borrowerName;
    }

    public String getInspector() {

        return inspector;
    }

    public void setInspector(String inspector) {

        this.inspector = inspector;
    }

    public String getSystemName() {

        return systemName;
    }

    public void setSystemName(String systemName) {

        this.systemName = systemName;
    }

    public String getSystemModel() {

        return systemModel;
    }

    public void setSystemModel(String systemModel) {

        this.systemModel = systemModel;
    }

    public String getCpu() {

        return cpu;
    }

    public void setCpu(String cpu) {

        this.cpu = cpu;
    }

    public String getGpu() {

        return gpu;
    }

    public void setGpu(String gpu) {

        this.gpu = gpu;
    }

    public Long getTotalRam() {

        return totalRam;
    }

    public void setTotalRam(Long totalRam) {

        this.totalRam = totalRam;
    }

    public Long getTotalStorage() {

        return totalStorage;
    }

    public void setTotalStorage(Long totalStorage) {

        this.totalStorage = totalStorage;
    }

    public Long getAvailableStorage() {

        return availableStorage;
    }

    public void setAvailableStorage(
            Long availableStorage
    ) {

        this.availableStorage = availableStorage;
    }

    public String getOs() {

        return os;
    }

    public void setOs(String os) {

        this.os = os;
    }

    public String getCheckType() {

        return checkType;
    }

    public void setCheckType(String checkType) {

        this.checkType = checkType;
    }

    public Double getBatteryHealth() {

        return batteryHealth;
    }

    public void setBatteryHealth(
            Double batteryHealth
    ) {

        this.batteryHealth = batteryHealth;
    }

    public Boolean getBatteryPresent() {

        return batteryPresent;
    }

    public void setBatteryPresent(
            Boolean batteryPresent
    ) {

        this.batteryPresent = batteryPresent;
    }

    public LocalDateTime getScanStartedAt() {

        return scanStartedAt;
    }

    public void setScanStartedAt(
            LocalDateTime scanStartedAt
    ) {

        this.scanStartedAt = scanStartedAt;
    }

    public LocalDateTime getSubmittedAt() {

        return submittedAt;
    }

    public void setSubmittedAt(
            LocalDateTime submittedAt
    ) {

        this.submittedAt = submittedAt;
    }

    public LocalDateTime getCheckTime() {

        return checkTime;
    }

    public void setCheckTime(
            LocalDateTime checkTime
    ) {

        this.checkTime = checkTime;
    }

    // =========================================
    // DISPLAY HELPERS
    // =========================================

    public String getTotalRamDisplay() {

        if (totalRam == null) {
            return "N/A";
        }

        double gb =
                totalRam
                        / 1024.0
                        / 1024.0
                        / 1024.0;

        return String.format(
                "%.2f GB",
                gb
        );
    }

    public String getTotalStorageDisplay() {

        if (totalStorage == null) {
            return "N/A";
        }

        double gb =
                totalStorage
                        / 1024.0
                        / 1024.0
                        / 1024.0;

        return String.format(
                "%.2f GB",
                gb
        );
    }

    public String getAvailableStorageDisplay() {

        if (availableStorage == null) {
            return "N/A";
        }

        double gb =
                availableStorage
                        / 1024.0
                        / 1024.0
                        / 1024.0;

        return String.format(
                "%.2f GB",
                gb
        );
    }

    public String getBatteryHealthDisplay() {

        if (batteryHealth == null) {
            return "N/A";
        }

        return String.format(
                "%.0f%%",
                batteryHealth
        );
    }

    // =========================================
    // TIMESTAMP DISPLAY
    // =========================================

    public String getCheckTimeDisplay() {

        return formatDateTime(checkTime);
    }

    public String getScanStartedAtDisplay() {

        return formatDateTime(scanStartedAt);
    }

    public String getSubmittedAtDisplay() {

        return formatDateTime(submittedAt);
    }

    // =========================================
    // FORMATTER
    // =========================================

    private String formatDateTime(
            LocalDateTime dateTime
    ) {

        if (dateTime == null) {
            return "-";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy HH:mm:ss"
                );

        return dateTime.format(formatter);
    }
}