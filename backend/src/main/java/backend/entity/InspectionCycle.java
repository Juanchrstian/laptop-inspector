package backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_cycles")
@Data
public class InspectionCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cycleId;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String borrowerName;

    @Column(nullable = false)
    private String status;

    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;
}