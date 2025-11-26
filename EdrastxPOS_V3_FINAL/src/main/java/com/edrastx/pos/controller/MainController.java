package com.edrastx.pos.controller;

import com.edrastx.pos.util.ExcelReportUtil;
import com.edrastx.pos.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.nio.file.Path;
import java.time.LocalDate;

public class MainController {
    @FXML
    private BorderPane rootPane;
    @FXML
    private Label lblUser;
    @FXML
    private Button btnProducts;
    @FXML
    private Button btnClients;
    @FXML
    private Button btnSales;
    @FXML
    private Button btnReport;

    @FXML
    private void initialize() {
        lblUser.setText(SessionManager.getCurrentUser() + " (" + SessionManager.getCurrentRole() + ")");
        if ("VENDEDOR".equalsIgnoreCase(SessionManager.getCurrentRole())) {
            btnProducts.setVisible(false);
            btnClients.setVisible(false);
            btnReport.setVisible(false);
        }
        showSales();
    }

    @FXML
    private void showProducts() {
        if (!btnProducts.isVisible()) {
            return;
        }
        loadCenter("/fxml/products.fxml");
    }

    @FXML
    private void showClients() {
        if (!btnClients.isVisible()) {
            return;
        }
        loadCenter("/fxml/clients.fxml");
    }

    @FXML
    private void showSales() {
        loadCenter("/fxml/sales.fxml");
    }

    @FXML
    private void exportReport() {
        if (!btnReport.isVisible()) {
            return;
        }
        Path file = ExcelReportUtil.exportDailySales(LocalDate.now());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (file != null) {
            alert.setTitle("Reporte generado");
            alert.setHeaderText("Reporte de ventas del día generado");
            alert.setContentText("Archivo: " + file.toAbsolutePath());
        } else {
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo generar el reporte");
            alert.setContentText("Revise la consola para más detalles.");
        }
        alert.showAndWait();
    }

    @FXML
    private void logout() {
        try {
            SessionManager.setUser(null, null);
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            rootPane.getScene().setRoot(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCenter(String resource) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(resource));
            rootPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
