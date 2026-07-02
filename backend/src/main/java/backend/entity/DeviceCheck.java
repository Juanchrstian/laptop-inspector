package backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_checks")
@Data
public class DeviceCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String inspectionCycleId;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String serialNumber;

    private String username;

    private String borrowerName;

    private String inspector;

    private String systemName;

    private String systemModel;

    @Column(length = 1000)
    private String cpu;

    @Column(length = 1000)
    private String gpu;

    private Long totalRam;

    private Long totalStorage;

    private Long availableStorage;

    private String os;

    @Column(nullable = false)
    private String checkType;

    private Double batteryHealth;

    private Boolean batteryPresent;

    private LocalDateTime scanStartedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime checkTime;

    @PrePersist
    protected void onCreate() {

        if (checkTime == null) {
            checkTime = LocalDateTime.now();
        }

        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }
}