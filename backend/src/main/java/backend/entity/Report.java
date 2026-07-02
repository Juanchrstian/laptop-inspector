package backend.entity;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
public class Report {

    // =========================================
    // PRIMARY KEY
    // =========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================
    // INSPECTION CYCLE
    // =========================================

    @Column(nullable = false)
    private String inspectionCycleId;

    // =========================================
    // REPORT INFORMATION
    // =========================================

    @Column(nullable = false)
    private String reportType;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, length = 1000)
    private String filePath;

    // =========================================
    // TIMESTAMP
    // =========================================

    private LocalDateTime createdAt;

    // =========================================
    // AUTO TIMESTAMP
    // =========================================

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}