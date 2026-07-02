package com.laptopinspector.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.laptopinspector.model.DeviceCheck;
import com.laptopinspector.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

public class QuickHistoryController {

    @FXML
    private TableView<DeviceCheck> tableHistory;

    @FXML
    private TableColumn<DeviceCheck, Long> colId;

    @FXML
    private TableColumn<DeviceCheck, String> colSerial;

    @FXML
    private TableColumn<DeviceCheck, String> colModel;

    @FXML
    private TableColumn<DeviceCheck, String> colType;

    @FXML
    private TableColumn<DeviceCheck, String> colInspector;

    private final ObservableList<DeviceCheck> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        setupTable();

        loadData();
    }

    private void setupTable() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colSerial.setCellValueFactory(
                new PropertyValueFactory<>("serialNumber")
        );

        colModel.setCellValueFactory(
                new PropertyValueFactory<>("systemModel")
        );

        colType.setCellValueFactory(
                new PropertyValueFactory<>("checkType")
        );

        colInspector.setCellValueFactory(
                new PropertyValueFactory<>("inspector")
        );

        tableHistory.setItems(data);
    }

    private void loadData() {

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "http://localhost:8080/api/device-checks/recent"
                                    )
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            ObjectMapper mapper =
                    new ObjectMapper();

            mapper.findAndRegisterModules();

            List<DeviceCheck> result =
                    mapper.readValue(
                            response.body(),
                            new TypeReference<List<DeviceCheck>>() {}
                    );

            data.clear();

            data.addAll(result);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked() {

        SceneManager.switchTo("menu");
    }
}