package com.laptopinspector.controller;

import com.laptopinspector.model.DeviceCheck;
import com.laptopinspector.service.ApiService;
import com.laptopinspector.service.HardwareService;
import com.laptopinspector.util.SceneManager;
import com.laptopinspector.util.ThemeManager;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class DetailController
        implements SceneManager.DataReceiver<String> {

    // =========================================
    // UI COMPONENTS
    // =========================================

    @FXML
    private Label lblCheckType;

    @FXML
    private Label lblSerialNumber;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblCpu;

    @FXML
    private Label lblGpu;

    @FXML
    private Label lblRam;

    @FXML
    private Label lblTotalStorage;

    @FXML
    private Label lblAvailableStorage;

    @FXML
    private Label lblOs;

    @FXML
    private Label lblBattery;

    @FXML
    private Label lblSystemModel;

    @FXML
    private VBox loadingOverlay;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnToggleTheme;

    @FXML
    private Label lblScanStarted;

    // =========================================
    // DATA
    // =========================================

    private String checkType;

    private DeviceCheck scannedData;

    private LocalDateTime scanStartedAt;

    // =========================================
    // RECEIVE DATA
    // =========================================

    @Override
    public void receiveData(String checkType) {

        this.checkType = checkType;

        lblCheckType.setText(checkType);

        lblCheckType.getStyleClass().clear();

        lblCheckType.getStyleClass().add(

                "BEFORE".equalsIgnoreCase(checkType)

                        ? "badge-before"

                        : "badge-after"
        );

        startScan();
    }

    // =========================================
    // INITIALIZE
    // =========================================

    @FXML
    public void initialize() {

        if (btnToggleTheme != null) {

            btnToggleTheme.setText(

                    ThemeManager.getToggleLabel()
            );
        }
    }

    // =========================================
    // THEME
    // =========================================

    @FXML
    private void onToggleTheme() {

        ThemeManager.toggle(

                btnToggleTheme.getScene()
        );

        btnToggleTheme.setText(

                ThemeManager.getToggleLabel()
        );
    }

    // =========================================
    // START SCAN
    // =========================================

    private void startScan() {

        loadingOverlay.setVisible(true);

        btnSubmit.setDisable(true);

        // =====================================
        // START TIMESTAMP
        // =====================================

        scanStartedAt = LocalDateTime.now();

        Thread scanThread = new Thread(() -> {

            try {

                HardwareService hardwareService =
                        new HardwareService();

                DeviceCheck result =
                        hardwareService.scan();

                // ==============================
                // INSPECTION INFO
                // ==============================

                result.setCheckType(checkType);

                result.setScanStartedAt(
                        scanStartedAt
                );

                // ==============================
                // STABLE CYCLE ID
                // ==============================

                if ("BEFORE".equalsIgnoreCase(checkType)) {

                String cycleId =
                        "CYCLE-"
                                + result.getSerialNumber()
                                + "-"
                                + System.currentTimeMillis();

                result.setInspectionCycleId(
                        cycleId
                );
                }

                // ==============================
                // UPDATE UI
                // ==============================

                Platform.runLater(() -> {

                    scannedData = result;

                    populateUI(result);

                    loadingOverlay.setVisible(false);

                    btnSubmit.setDisable(false);

                    ThemeManager.applyTheme(
                            btnSubmit.getScene()
                    );
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    loadingOverlay.setVisible(false);

                    btnSubmit.setDisable(true);

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.ERROR
                            );

                    alert.setTitle(
                            "Scan Error"
                    );

                    alert.setHeaderText(
                            "Gagal melakukan scan hardware"
                    );

                    alert.setContentText(
                            e.getMessage()
                    );

                    alert.showAndWait();
                });
            }

        });

        scanThread.setDaemon(true);

        scanThread.start();
    }

    // =========================================
    // POPULATE UI
    // =========================================

    private void populateUI(DeviceCheck dto) {

        lblSerialNumber.setText(

                safe(dto.getSerialNumber())
        );

        lblUsername.setText(

                safe(dto.getUsername())
        );

        lblCpu.setText(

                safe(dto.getCpu())
        );

        lblGpu.setText(

                safe(dto.getGpu())
        );

        lblRam.setText(

                dto.getTotalRamDisplay()
        );

        lblTotalStorage.setText(

                dto.getTotalStorageDisplay()
        );

        lblAvailableStorage.setText(

                dto.getAvailableStorageDisplay()
        );

        lblOs.setText(

                safe(dto.getOs())
        );

        lblBattery.setText(

                dto.getBatteryHealthDisplay()
        );

        lblSystemModel.setText(

                safe(dto.getSystemName())
                        + " "
                        + safe(dto.getSystemModel())
        );
        lblScanStarted.setText(

        dto.getScanStartedAtDisplay()
        );
    }

    // =========================================
    // SUBMIT
    // =========================================

    @FXML
    private void onSubmitClicked() {

        if (scannedData == null) {

            return;
        }

        openInspectorPopup();
    }

    // =========================================
    // BACK
    // =========================================

    @FXML
    private void onBackClicked() {

        SceneManager.switchTo("menu");
    }

    // =========================================
    // POPUP
    // =========================================

    private void openInspectorPopup() {

        try {

                if ("AFTER".equalsIgnoreCase(checkType)) {

                ApiService apiService =
                        new ApiService();

                DeviceCheck beforeData =
                        apiService
                                .getLatestBeforeInspection(
                                        scannedData.getSerialNumber()
                                );

                if (beforeData == null) {

                        showError(
                                "Data inspeksi awal tidak ditemukan.\n"
                                        + "Laptop belum pernah dipinjam."
                        );

                        return;
                }

                scannedData.setBorrowerName(
                        beforeData.getBorrowerName()
                );

                scannedData.setInspectionCycleId(
                        beforeData.getInspectionCycleId()
                );
                }

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/com/laptopinspector/fxml/inspector_popup.fxml"
                                )
                        );

                Parent root =
                        loader.load();

                InspectorPopupController popupController =
                        loader.getController();

                popupController.setScannedData(
                        scannedData
                );

                Stage popupStage =
                        new Stage();

                popupStage.initModality(
                        Modality.APPLICATION_MODAL
                );

                popupStage.initOwner(
                        SceneManager.getPrimaryStage()
                );

                popupStage.setTitle(
                        "Formulir Peminjaman Laptop"
                );

                popupStage.setResizable(false);

                Scene scene =
                        new Scene(root);

                scene.getStylesheets().add(
                        getClass()
                                .getResource(
                                        "/com/laptopinspector/css/cyberpunk.css"
                                )
                                .toExternalForm()
                );

                ThemeManager.applyTheme(scene);

                popupStage.setScene(scene);

                popupStage.showAndWait();

        } catch (Exception e) {

                e.printStackTrace();

                showError(
                        "Gagal membuka form inspeksi."
                );
        }
        }
    // =========================================
    // SAFE STRING
    // =========================================

    private String safe(String value) {

        return value == null || value.isBlank()

                ? "N/A"

                : value;
    }

    private void showError(
                String message
        ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Error"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        ThemeManager.applyTheme(
                alert.getDialogPane()
                        .getScene()
        );

        alert.showAndWait();
        }
}