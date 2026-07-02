package com.laptopinspector.controller;

import com.laptopinspector.util.SceneManager;
import com.laptopinspector.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingController {

    @FXML private Button btnStart;
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
    private void onStartClicked() {
        SceneManager.switchTo("menu");
    }
}
