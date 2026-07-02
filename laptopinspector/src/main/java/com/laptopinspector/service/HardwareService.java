package com.laptopinspector.service;

import com.laptopinspector.model.DeviceCheck;

import oshi.SystemInfo;

import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;

import oshi.software.os.OperatingSystem;
import oshi.software.os.OSFileStore;

import java.util.List;

public class HardwareService {

    private final SystemInfo systemInfo;

    private final HardwareAbstractionLayer hardware;

    private final OperatingSystem operatingSystem;

    public HardwareService() {

        this.systemInfo = new SystemInfo();

        this.hardware = systemInfo.getHardware();

        this.operatingSystem =
                systemInfo.getOperatingSystem();
    }

    public DeviceCheck scan() {

        DeviceCheck dto = new DeviceCheck();

        System.out.println("===== START HARDWARE SCAN =====");

        // =================================================
        // SYSTEM
        // =================================================

        try {

            ComputerSystem cs =
                    hardware.getComputerSystem();

            String serial =
                    cs.getSerialNumber();

            if (serial == null
                    || serial.isBlank()
                    || serial.equalsIgnoreCase("unknown")) {

                serial =
                        "SN-"
                                + System.getProperty(
                                "user.name"
                        ).hashCode();
            }

            dto.setSerialNumber(serial);

            dto.setDeviceId(serial);

            dto.setSystemName(
                    safe(cs.getManufacturer())
            );

            dto.setSystemModel(
                    safe(cs.getModel())
            );

        } catch (Exception e) {

            e.printStackTrace();

            dto.setSerialNumber("UNKNOWN");

            dto.setDeviceId("UNKNOWN");

            dto.setSystemName("UNKNOWN");

            dto.setSystemModel("UNKNOWN");
        }

        // =================================================
        // USERNAME
        // =================================================

        dto.setUsername(
                System.getProperty("user.name")
        );

        // =================================================
        // CPU
        // =================================================

        try {

            System.out.println("Scanning CPU...");

            CentralProcessor cpu =
                    hardware.getProcessor();

            dto.setCpu(
                    safe(
                            cpu.getProcessorIdentifier()
                                    .getName()
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            dto.setCpu("N/A");
        }

        // =================================================
        // GPU
        // =================================================

        try {

            System.out.println("Scanning GPU...");

            List<GraphicsCard> gpuList =
                    hardware.getGraphicsCards();

            if (!gpuList.isEmpty()) {

                dto.setGpu(
                        safe(
                                gpuList.get(0).getName()
                        )
                );

            } else {

                dto.setGpu("N/A");
            }

        } catch (Exception e) {

            e.printStackTrace();

            dto.setGpu("N/A");
        }

        // =================================================
        // RAM
        // =================================================

        try {

            System.out.println("Scanning RAM...");

            GlobalMemory memory =
                    hardware.getMemory();

            long totalRam =
                    memory.getTotal();

            dto.setTotalRam(totalRam);

        } catch (Exception e) {

            e.printStackTrace();

            dto.setTotalRam(0L);
        }

        // =================================================
        // TOTAL STORAGE
        // =================================================

        try {

            System.out.println("Scanning Storage...");

            long totalStorage = 0L;

            List<HWDiskStore> disks =
                    hardware.getDiskStores();

            for (HWDiskStore disk : disks) {

                totalStorage += disk.getSize();
            }

            dto.setTotalStorage(totalStorage);

        } catch (Exception e) {

            e.printStackTrace();

            dto.setTotalStorage(0L);
        }

        // =================================================
        // AVAILABLE STORAGE
        // =================================================

        try {

            System.out.println("Scanning Available Storage...");

            long availableStorage = 0L;

            List<OSFileStore> stores =
                    operatingSystem
                            .getFileSystem()
                            .getFileStores();

            for (OSFileStore fs : stores) {

                String mount = fs.getMount();

                if (mount != null
                        && mount.startsWith("C:")) {

                    availableStorage +=
                            fs.getUsableSpace();
                }
            }

            dto.setAvailableStorage(
                    availableStorage
            );

        } catch (Exception e) {

            e.printStackTrace();

            dto.setAvailableStorage(0L);
        }

        // =================================================
        // OS
        // =================================================

        try {

            dto.setOs(
                    safe(operatingSystem.toString())
            );

        } catch (Exception e) {

            e.printStackTrace();

            dto.setOs("Unknown OS");
        }

        // =================================================
        // BATTERY
        // =================================================

        try {

            System.out.println("Scanning Battery...");

            List<PowerSource> batteries =
                    hardware.getPowerSources();

            if (!batteries.isEmpty()) {

                PowerSource battery =
                        batteries.get(0);

                dto.setBatteryPresent(true);

                dto.setBatteryHealth(
                        battery.getRemainingCapacityPercent()
                                * 100.0
                );

            } else {

                dto.setBatteryPresent(false);

                dto.setBatteryHealth(null);
            }

        } catch (Exception e) {

            e.printStackTrace();

            dto.setBatteryPresent(false);

            dto.setBatteryHealth(null);
        }

        System.out.println("===== SCAN COMPLETE =====");

        return dto;
    }

    private String safe(String value) {

        if (value == null || value.isBlank()) {
            return "N/A";
        }

        return value.trim();
    }
}