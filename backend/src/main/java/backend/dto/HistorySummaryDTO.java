package backend.dto;

public class HistorySummaryDTO {

    private String inspectionCycleId;

    private String serialNumber;

    private String borrowerName;

    private String beforeTime;

    private String afterTime;

    private String status;

    public HistorySummaryDTO() {
    }

    public String getInspectionCycleId() {
        return inspectionCycleId;
    }

    public void setInspectionCycleId(
            String inspectionCycleId
    ) {
        this.inspectionCycleId = inspectionCycleId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(
            String serialNumber
    ) {
        this.serialNumber = serialNumber;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(
            String borrowerName
    ) {
        this.borrowerName = borrowerName;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(
            String beforeTime
    ) {
        this.beforeTime = beforeTime;
    }

    public String getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(
            String afterTime
    ) {
        this.afterTime = afterTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }
}