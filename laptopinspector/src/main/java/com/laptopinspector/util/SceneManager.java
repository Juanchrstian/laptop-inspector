package com.laptopinspector.util;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.Objects;

public class SceneManager {

    private static Stage primaryStage;

    public static void init(Stage stage) {

        primaryStage = stage;
    }

    public static void switchTo(String fxmlName) {

        try {

            Scene scene =
                    loadScene(fxmlName);

            if (scene == null) {
                return;
            }

            primaryStage.setScene(scene);

            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static <T> void switchTo(
            String fxmlName,
            T data
    ) {

        try {

            URL fxmlUrl =
                    SceneManager.class.getResource(
                            "/com/laptopinspector/fxml/"
                                    + fxmlName
                                    + ".fxml"
                    );

            if (fxmlUrl == null) {

                throw new RuntimeException(
                        "FXML tidak ditemukan: "
                                + fxmlName
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(fxmlUrl);

            Parent root =
                    loader.load();

            Object controller =
                    loader.getController();

            if (controller instanceof DataReceiver<?>) {

                @SuppressWarnings("unchecked")

                DataReceiver<T> receiver =
                        (DataReceiver<T>) controller;

                receiver.receiveData(data);
            }

            Scene scene =
                    new Scene(root);

            applyCss(scene);

            ThemeManager.applyTheme(scene);

            primaryStage.setScene(scene);

            primaryStage.centerOnScreen();

            primaryStage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static Scene loadScene(
            String fxmlName
    ) throws IOException {

        URL fxmlUrl =
                SceneManager.class.getResource(
                        "/com/laptopinspector/fxml/"
                                + fxmlName
                                + ".fxml"
                );

        if (fxmlUrl == null) {

            throw new RuntimeException(
                    "FXML tidak ditemukan: "
                            + fxmlName
            );
        }

        FXMLLoader loader =
                new FXMLLoader(fxmlUrl);

        Parent root =
                loader.load();

        Scene scene =
                new Scene(root);

        applyCss(scene);

        ThemeManager.applyTheme(scene);

        return scene;
    }

    private static void applyCss(Scene scene) {

        scene.getStylesheets().add(

                Objects.requireNonNull(

                        SceneManager.class.getResource(
                                "/com/laptopinspector/css/cyberpunk.css"
                        )

                ).toExternalForm()
        );
    }

    public static Stage getPrimaryStage() {

        return primaryStage;
    }

    public interface DataReceiver<T> {

        void receiveData(T data);
    }
}