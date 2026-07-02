package com.laptopinspector.controller;

import com.laptopinspector.model.DeviceCheck;
import com.laptopinspector.model.Employee;
import com.laptopinspector.model.ItStaff;
import com.laptopinspector.model.Report;
import com.laptopinspector.service.ApiService;
import com.laptopinspector.util.SceneManager;
import com.laptopinspector.util.ThemeManager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class InspectorPopupController {

    // =========================================
    // UI COMPONENTS
    // =========================================

    @FXML
    private ComboBox<ItStaff> cmbInspector;

    @FXML
    private ComboBox<Employee> cmbBorrower;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    // =========================================
    // SERVICE
    // =========================================

    private final ApiService apiService =
            new ApiService();

    // =========================================
    // DATA
    // =========================================

    private DeviceCheck scannedData;

    // =========================================
    // INITIALIZE
    // =========================================

    @FXML
    private void initialize() {

        loadInspectors();

        loadEmployees();
    }

    // =========================================
    // LOAD IT STAFF
    // =========================================

    private void loadInspectors() {

        cmbInspector.setItems(

                FXCollections.observableArrayList(

                        apiService.getItStaff()

                )
        );
    }

    // =========================================
    // LOAD EMPLOYEE
    // =========================================

    private void loadEmployees() {

        cmbBorrower.setItems(

                FXCollections.observableArrayList(

                        apiService.getEmployees()

                )
        );
    }

    // =========================================
    // SET SCANNED DATA
    // =========================================

    public void setScannedData(
            DeviceCheck scannedData
    ) {

        this.scannedData = scannedData;

        if (scannedData == null) {
            return;
        }

        // =====================================
        // AFTER INSPECTION
        // =====================================

        if ("AFTER".equalsIgnoreCase(
                scannedData.getCheckType()
        )) {

            for (Employee employee :
                    cmbBorrower.getItems()) {

                if (employee.getFullName()
                        .equals(
                                scannedData.getBorrowerName()
                        )) {

                    cmbBorrower.setValue(
                            employee
                    );

                    break;
                }
            }

            cmbBorrower.setDisable(true);
        }

        // =====================================
        // BEFORE INSPECTION
        // =====================================

        else {

            cmbBorrower.setDisable(false);
        }
    }
    
        // =========================================
        // CONFIRM
        // =========================================

        @FXML
        private void onConfirmClicked() {

                // =====================================
                // VALIDATION
                // =====================================

                if (scannedData == null) {

                showError(
                        "Data scan tidak ditemukan"
                );

                return;
                }

                ItStaff selectedStaff =
                        cmbInspector.getValue();

                if (selectedStaff == null) {

                showError(
                        "Pilih Petugas IT."
                );

                return;
                }

                Employee selectedEmployee =
                        cmbBorrower.getValue();

                if ("BEFORE".equalsIgnoreCase(
                        scannedData.getCheckType()
                )) {

                if (selectedEmployee == null) {

                        showError(
                                "Pilih Karyawan."
                        );

                        return;
                }
                }

                // =====================================
                // SET DATA
                // =====================================

                scannedData.setInspector(
                        selectedStaff.getFullName()
                );

                if ("BEFORE".equalsIgnoreCase(
                        scannedData.getCheckType()
                )) {

                scannedData.setBorrowerName(
                        selectedEmployee.getFullName()
                );
                }

                scannedData.setSubmittedAt(
                        LocalDateTime.now()
                );

                // =====================================
                // DISABLE BUTTON
                // =====================================

                btnConfirm.setDisable(true);

                // =====================================
                // SUBMIT
                // =====================================

                DeviceCheck saved = null;

                try {

                saved = apiService.submitDeviceCheck(
                        scannedData
                );

                } catch (RuntimeException ex) {

                btnConfirm.setDisable(false);

                showError(
                        ex.getMessage()
                );

                return;

                } catch (Exception ex) {

                btnConfirm.setDisable(false);

                showError(
                        "Tidak dapat terhubung ke server."
                );

                return;
                }

                // =====================================
                // SUCCESS
                // =====================================

                scannedData = saved;

                showSuccess(
                        "Data berhasil disimpan"
                );

                // =====================================
                // OPEN REPORT PDF
                // =====================================

                if (scannedData.getInspectionCycleId() != null
                        && !scannedData.getInspectionCycleId().isBlank()) {

                List<Report> reports =
                        apiService.getReportsByCycleId(
                                scannedData.getInspectionCycleId()
                        );

                if (!reports.isEmpty()) {

                        Report latest =
                                reports.get(
                                        reports.size() - 1
                                );

                        apiService.openReport(
                                latest.getId()
                        );
                }
                }

                closePopup();

                SceneManager.switchTo(
                        "notification"
                );
        }

    // =========================================
    // CANCEL
    // =========================================

    @FXML
    private void onCancelClicked() {

        closePopup();
    }

    // =========================================
    // CLOSE POPUP
    // =========================================

    private void closePopup() {

        Stage stage =
                (Stage) btnCancel
                        .getScene()
                        .getWindow();

        stage.close();
    }

    // =========================================
    // SUCCESS
    // =========================================

    private void showSuccess(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Success"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        ThemeManager.applyTheme(
                alert.getDialogPane()
                        .getScene()
        );

        alert.showAndWait();
    }

    // =========================================
    // ERROR
    // =========================================

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

        alert.setHeaderText(null);

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