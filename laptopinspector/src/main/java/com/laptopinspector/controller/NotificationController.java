package com.laptopinspector.controller;

import com.laptopinspector.util.SceneManager;
import com.laptopinspector.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificationController implements SceneManager.DataReceiver<String> {

    @FXML private Label lblStatus;
    @FXML private Label lblMessage;
    @FXML private VBox iconSuccess;
    @FXML private VBox iconFailed;
    @FXML private Button btnToggleTheme;

    @Override
    public void receiveData(String status) {
        boolean isSuccess = "SUCCESS".equals(status);

        if (isSuccess) {
            lblStatus.setText("Data Tersimpan");
            lblStatus.getStyleClass().setAll("notif-title-success");
            lblMessage.setText("Data inspeksi berhasil dikirim ke server.");
            iconSuccess.setVisible(true);
            iconFailed.setVisible(false);
        } else {
            lblStatus.setText("Gagal Mengirim");
            lblStatus.getStyleClass().setAll("notif-title-failed");
            lblMessage.setText("Tidak dapat terhubung ke server. Pastikan backend berjalan di port 8080.");
            iconSuccess.setVisible(false);
            iconFailed.setVisible(true);
        }

        // Apply current theme
        if (btnToggleTheme != null) {
            ThemeManager.applyTheme(btnToggleTheme.getScene());
        }
    }

    @FXML
    public void initialize() {
        if (btnToggleTheme != null) {
            btnToggleTheme.setText(ThemeManager.getToggleLabel());
        }
    }

    @FXML
    private void onToggleTheme() {
        ThemeManager.toggle(btnToggleTheme.getScene());
        btnToggleTheme.setText(ThemeManager.getToggleLabel());
    }

    @FXML
    private void onBackToMenuClicked() {
        SceneManager.switchTo("menu");
    }

    @FXML
    private void onNewInspectionClicked() {
        SceneManager.switchTo("menu");
    }
}
