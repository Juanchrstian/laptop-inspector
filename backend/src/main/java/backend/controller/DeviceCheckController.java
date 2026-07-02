package backend.controller;

import backend.dto.HistorySummaryDTO;
import backend.entity.DeviceCheck;
import backend.repository.DeviceCheckRepository;
import backend.service.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/api/device-checks")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DeviceCheckController {

private final DeviceCheckRepository repository;
private final ReportService reportService;

// =========================================
// SAVE
// =========================================

@PostMapping
public DeviceCheck save(
        @RequestBody DeviceCheck deviceCheck
) {

    if (deviceCheck == null) {

        throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
                "Device check data is required."
        );
    }

    String type =
            deviceCheck.getCheckType();

    if (type == null
            || type.isBlank()) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Check type is required."
        );
    }

    // =====================================
    // AUTO TIMESTAMP
    // =====================================

    if (deviceCheck.getCheckTime() == null) {

        deviceCheck.setCheckTime(
                LocalDateTime.now()
        );
    }

    if (deviceCheck.getSubmittedAt() == null) {

        deviceCheck.setSubmittedAt(
                LocalDateTime.now()
        );
    }

    // =====================================
    // BEFORE
    // =====================================

    if ("BEFORE".equalsIgnoreCase(type)) {

        String serial = deviceCheck.getSerialNumber();

        if (serial == null || serial.isBlank()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Serial number is required."
                );
        }

        Optional<DeviceCheck> latest =
                repository.findTopBySerialNumberOrderBySubmittedAtDesc(
                        serial.trim()
                );

        // =====================================
        // FIRST INSPECTION
        // =====================================

        if (latest.isEmpty()) {

                deviceCheck.setInspectionCycleId(
                        "CYCLE-" + System.currentTimeMillis()
                );

                DeviceCheck saved =
                        repository.save(deviceCheck);

                generatePdfReport(saved);

                return saved;
        }

        DeviceCheck lastCheck = latest.get();

        // =====================================
        // LAST = BEFORE
        // =====================================

        if ("BEFORE".equalsIgnoreCase(
                lastCheck.getCheckType()
        )) {

                throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Laptop masih dipinjam dan belum dikembalikan."
                        );
        }

        // =====================================
        // LAST = AFTER
        // =====================================

        deviceCheck.setInspectionCycleId(
                "CYCLE-" + System.currentTimeMillis()
        );

        DeviceCheck saved =
                repository.save(deviceCheck);

        generatePdfReport(saved);

        return saved;
        }

    // =====================================
    // AFTER
    // =====================================

    if ("AFTER".equalsIgnoreCase(type)) {

        String serial = deviceCheck.getSerialNumber();

        if (serial == null || serial.isBlank()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Serial number is required."
                );
        }

        Optional<DeviceCheck> latest =
                repository.findTopBySerialNumberOrderBySubmittedAtDesc(
                        serial.trim()
                );

        if (latest.isEmpty()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Laptop belum pernah dipinjam."
                );
        }

        DeviceCheck lastCheck = latest.get();

        if ("AFTER".equalsIgnoreCase(
                lastCheck.getCheckType()
        )) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Laptop sudah dikembalikan."
                );
        }

        deviceCheck.setInspectionCycleId(
                lastCheck.getInspectionCycleId()
        );

        if (deviceCheck.getBorrowerName() == null
                || deviceCheck.getBorrowerName().isBlank()) {

                deviceCheck.setBorrowerName(
                        lastCheck.getBorrowerName()
                );
        }

        DeviceCheck saved =
                repository.save(deviceCheck);

        generatePdfReport(saved);

        return saved;
        }

    throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Check type is required."
        );
}

// =========================================
// GET ALL
// =========================================

@GetMapping
public List<DeviceCheck> getAll() {

    return repository.findAll();
}

// =========================================
// RECENT
// =========================================

@GetMapping("/recent")
public List<DeviceCheck> getRecent() {

    return repository
            .findTop10ByOrderByIdDesc();
}

// =========================================
// COMPARE
// =========================================

@GetMapping("/compare/{cycleId}")
public List<DeviceCheck> compare(
        @PathVariable String cycleId
) {

    return repository
            .findByInspectionCycleIdOrderByCheckTimeAsc(
                    cycleId
            );
}

// =========================================
// HISTORY SUMMARY
// =========================================

@GetMapping("/history-summary")
public List<HistorySummaryDTO>
getHistorySummary() {

    List<DeviceCheck> all =
            repository.findAll();

    Map<String, List<DeviceCheck>> grouped =

            all.stream()

                    .filter(dc ->
                            dc.getInspectionCycleId()
                                    != null
                    )

                    .collect(
                            Collectors.groupingBy(
                                    DeviceCheck
                                            ::getInspectionCycleId
                            )
                    );

    List<HistorySummaryDTO> result =
            new ArrayList<>();

    for (String cycleId : grouped.keySet()) {

        List<DeviceCheck> items =
                grouped.get(cycleId);

        items.sort(
                Comparator.comparing(
                        DeviceCheck::getCheckTime,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );

        DeviceCheck before = null;
        DeviceCheck after = null;

        for (DeviceCheck dc : items) {

            if ("BEFORE".equalsIgnoreCase(
                    dc.getCheckType()
            )) {
                before = dc;
            }

            if ("AFTER".equalsIgnoreCase(
                    dc.getCheckType()
            )) {
                after = dc;
            }
        }

        HistorySummaryDTO dto =
                new HistorySummaryDTO();

        dto.setInspectionCycleId(
                cycleId
        );

        if (before != null) {

            dto.setSerialNumber(
                    safe(before.getSerialNumber())
            );

            dto.setBorrowerName(
                    safe(before.getBorrowerName())
            );

            dto.setBeforeTime(
                    formatTime(
                            before.getSubmittedAt()
                    )
            );
        }

        if (after != null) {

            dto.setAfterTime(
                    formatTime(
                            after.getSubmittedAt()
                    )
            );
        }

        dto.setStatus(
                after != null
                        ? "COMPLETED"
                        : "Dipinjam"
        );

        result.add(dto);
    }

    result.sort(
            (a, b) ->
                    b.getBeforeTime()
                            .compareTo(
                                    a.getBeforeTime()
                            )
    );

    return result;
}

// =========================================
// GET LATEST BEFORE
// =========================================

@GetMapping("/before/{serialNumber}")
public ResponseEntity<DeviceCheck>
getLatestBeforeInspection(
        @PathVariable String serialNumber
) {

    Optional<DeviceCheck> latest =
            repository.findTopBySerialNumberOrderBySubmittedAtDesc(
                    serialNumber.trim()
            );

    if (latest.isEmpty()) {

        return ResponseEntity.notFound().build();
    }

    DeviceCheck last = latest.get();

    if (!"BEFORE".equalsIgnoreCase(
            last.getCheckType()
    )) {

        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(last);
}

// =========================================
// PDF REPORT
// =========================================

private void generatePdfReport(
        DeviceCheck saved
) {

    try {

        reportService.generateReport(
                saved
        );

    }

    catch (Exception e) {

        System.err.println(
                "Failed to generate PDF report : "
                        + e.getMessage()
        );

        e.printStackTrace();
    }
}

// =========================================
// UTIL
// =========================================

private String formatTime(
        LocalDateTime time
) {

    if (time == null) {
        return "-";
    }

    return time.toString();
}

private String safe(
        String value
) {

    return value == null
            || value.isBlank()
            ? "-"
            : value;
}

}
