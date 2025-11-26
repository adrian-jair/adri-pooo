package com.edrastx.pos.controller;

import com.edrastx.pos.MainApp;
import com.edrastx.pos.dao.UserDAO;
import com.edrastx.pos.model.User;
import com.edrastx.pos.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void onLogin(ActionEvent event) {
        String user = txtUser.getText();
        String pass = txtPassword.getText();
        User u = userDAO.login(user, pass);
        if (u == null) {
            lblError.setText("Usuario o contraseña incorrectos");
            return;
        }
        SessionManager.setUser(u.getUsername(), u.getRole());
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 720);
            scene.getStylesheets().add(MainApp.class.getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EdrastxPOS");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
