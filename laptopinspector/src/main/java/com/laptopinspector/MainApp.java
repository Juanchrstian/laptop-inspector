package com.laptopinspector;

import com.laptopinspector.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.init(primaryStage);
        SceneManager.switchTo("landing");
        primaryStage.setTitle("Laptop Inspector");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
