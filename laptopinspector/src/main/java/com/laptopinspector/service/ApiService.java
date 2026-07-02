package com.laptopinspector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.laptopinspector.model.DeviceCheck;
import com.laptopinspector.model.Report;
import com.laptopinspector.model.Employee;
import com.laptopinspector.model.ItStaff;

import java.awt.Desktop;
import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

public class ApiService {

    // =========================================
    // API URL
    // =========================================

    private static final String API_URL =
            "http://localhost:8080/api/device-checks";

    private static final String REPORT_URL =
            "http://localhost:8080/api/reports";


    private static final String EMPLOYEE_URL =
        "http://localhost:8080/api/employees";

    private static final String STAFF_URL =
        "http://localhost:8080/api/it-staff";       

    // =========================================
    // HTTP
    // =========================================

    private final HttpClient client;
    private final ObjectMapper mapper;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public ApiService() {

        client = HttpClient.newHttpClient();

        mapper = new ObjectMapper();

        mapper.registerModule(
                new JavaTimeModule()
        );
    }

    // =========================================
    // SUBMIT DEVICE CHECK
    // =========================================

    public DeviceCheck submitDeviceCheck(
            DeviceCheck deviceCheck
    ) {

        try {

            String json =
                    mapper.writeValueAsString(
                            deviceCheck
                    );

            System.out.println(
                    "SEND JSON:"
            );

            System.out.println(
                    json
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            API_URL
                                    )
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest
                                            .BodyPublishers
                                            .ofString(
                                                    json
                                            )
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );

            System.out.println(
                    "STATUS:"
            );

            System.out.println(
                    response.statusCode()
            );

            System.out.println(
                    "BODY:"
            );

            System.out.println(
                    response.body()
            );

            if (response.statusCode() != 200
                && response.statusCode() != 201) {

        try {

                var errorJson =
                        mapper.readTree(
                                response.body()
                        );

                if (errorJson.has("detail")) {

                        throw new RuntimeException(
                                errorJson.get("detail").asText()
                        );
                }

                if (errorJson.has("message")) {

                        throw new RuntimeException(
                                errorJson.get("message").asText()
                        );
                }

                throw new RuntimeException(
                        response.body()
                );

                }
                catch (IOException e) {

                throw new RuntimeException(
                        "Tidak dapat terhubung ke server."
                );
                }
        }

            return mapper.readValue(
                    response.body(),
                    DeviceCheck.class
            );

        }

        catch (
                IOException
                | InterruptedException e
        ) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================
    // GET LATEST BEFORE
    // =========================================

    public DeviceCheck getLatestBeforeInspection(
            String serialNumber
    ) {

        try {

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            API_URL
                                                    + "/before/"
                                                    + serialNumber
                                    )
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );

            if (response.statusCode() != 200) {

                return null;
            }

            return mapper.readValue(
                    response.body(),
                    DeviceCheck.class
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================
    // GET REPORTS BY CYCLE ID
    // =========================================

    public List<Report> getReportsByCycleId(
            String cycleId
    ) {

        try {

            if (cycleId == null
                    || cycleId.isBlank()) {

                return List.of();
            }

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            REPORT_URL
                                                    + "/cycle/"
                                                    + cycleId
                                    )
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );

            if (response.statusCode() != 200) {

                return List.of();
            }

            return mapper.readValue(
                    response.body(),

                    mapper
                            .getTypeFactory()
                            .constructCollectionType(
                                    List.class,
                                    Report.class
                            )
            );

        }

        catch (Exception e) {

            e.printStackTrace();

            return List.of();
        }
    }

    // =========================================
    // DOWNLOAD URL
    // =========================================

    public String getReportDownloadUrl(
            Long reportId
    ) {

        return REPORT_URL
                + "/download/"
                + reportId;
    }

    // =========================================
    // OPEN REPORT
    // =========================================

    public void openReport(
            Long reportId
    ) {

        try {

            Desktop.getDesktop().browse(
                    URI.create(
                            getReportDownloadUrl(
                                    reportId
                            )
                    )
            );

        }

        catch (Exception e) {

            e.printStackTrace();
        }
    }

        // =========================================
        // GET ALL EMPLOYEES
        // =========================================

        public List<Employee> getEmployees() {

        try {

                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(
                                        URI.create(
                                                EMPLOYEE_URL
                                        )
                                )
                                .GET()
                                .build();

                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                if (response.statusCode() != 200) {

                return List.of();
                }

                return mapper.readValue(

                        response.body(),

                        mapper.getTypeFactory()
                                .constructCollectionType(
                                        List.class,
                                        Employee.class
                                )
                );

        }

        catch (Exception e) {

                e.printStackTrace();

                return List.of();
        }
        }

        // =========================================
        // GET ALL IT STAFF
        // =========================================

        public List<ItStaff> getItStaff() {

        try {

                HttpRequest request =
                        HttpRequest.newBuilder()
                                .uri(
                                        URI.create(
                                                STAFF_URL
                                        )
                                )
                                .GET()
                                .build();

                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                if (response.statusCode() != 200) {

                return List.of();
                }

                return mapper.readValue(

                        response.body(),

                        mapper.getTypeFactory()
                                .constructCollectionType(
                                        List.class,
                                        ItStaff.class
                                )
                );

        }

        catch (Exception e) {

                e.printStackTrace();

                return List.of();
        }
        }
}