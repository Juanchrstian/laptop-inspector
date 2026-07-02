package backend.repository;

import backend.entity.DeviceCheck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceCheckRepository
        extends JpaRepository<DeviceCheck, Long> {

    // =========================================
    // RECENT HISTORY
    // =========================================

    List<DeviceCheck>
    findTop10ByOrderByIdDesc();

    // =========================================
    // SERIAL HISTORY
    // =========================================

    List<DeviceCheck>
    findBySerialNumberOrderByCheckTimeAsc(
            String serialNumber
    );

    List<DeviceCheck>
    findBySerialNumberOrderByCheckTimeDesc(
            String serialNumber
    );

    // =========================================
    // LAST INSPECTION
    // =========================================

    Optional<DeviceCheck>
    findTopBySerialNumberOrderBySubmittedAtDesc(
            String serialNumber
    );

    // =========================================
    // CYCLE HISTORY
    // =========================================

    List<DeviceCheck>
    findByInspectionCycleId(
            String inspectionCycleId
    );

    List<DeviceCheck>
    findByInspectionCycleIdOrderByCheckTimeAsc(
            String inspectionCycleId
    );

    // =========================================
    // VALIDATION
    // =========================================

    boolean existsByInspectionCycleIdAndCheckType(
            String inspectionCycleId,
            String checkType
    );

    // =========================================
    // LEGACY
    // =========================================
    // Masih dipertahankan untuk kompatibilitas.
    // Nantinya dapat dihapus apabila sudah
    // tidak digunakan lagi.

    Optional<DeviceCheck>
    findTopBySerialNumberAndCheckTypeOrderBySubmittedAtDesc(
            String serialNumber,
            String checkType
    );
}