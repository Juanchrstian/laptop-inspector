package com.laptopinspector.util;

import javafx.scene.Scene;

/**
 * Menyimpan state tema (dark/light) secara global
 * agar konsisten di semua halaman.
 */
public class ThemeManager {

    private static boolean isDark = false;

    public static boolean isDark() {
        return isDark;
    }

    public static void toggle(Scene scene) {
        isDark = !isDark;
        applyTheme(scene);
    }

    public static void applyTheme(Scene scene) {
        if (isDark) {
            if (!scene.getRoot().getStyleClass().contains("dark-mode")) {
                scene.getRoot().getStyleClass().add("dark-mode");
            }
        } else {
            scene.getRoot().getStyleClass().remove("dark-mode");
        }
    }

    public static String getToggleLabel() {
        return isDark ? "☀ Light Mode" : "☾ Dark Mode";
    }
}
