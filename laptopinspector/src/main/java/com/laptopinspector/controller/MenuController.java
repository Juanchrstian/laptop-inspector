package com.laptopinspector.controller;

import com.laptopinspector.util.SceneManager;
import com.laptopinspector.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML private Button btnToggleTheme;

    @FXML
    public void initialize() {
        btnToggleTheme.setText(ThemeManager.getToggleLabel());
    }

    @FXML
    private void onToggleTheme() {
        ThemeManager.toggle(btnToggleTheme.getScene());
        btnToggleTheme.setText(ThemeManager.getToggleLabel());
    }

    @FXML
    private void onBeforeClicked() {
        SceneManager.switchTo("detail", "BEFORE");
    }

    @FXML
    private void onAfterClicked() {
        SceneManager.switchTo("detail", "AFTER");
    }

    @FXML
    private void onQuickHistoryClicked() {

        SceneManager.switchTo(
                "quick_history"
        );
    }
}
