package com.ipz.bills;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        try {
            String username = loginField.getText().trim();
            String password = passwordField.getText();

            if (username.isBlank() || password.isBlank()) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Enter your username and password."
                );
                return;
            }

            if (!UserStore.login(username, password)) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Incorrect username or password."
                );
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ipz/bills/main-view.fxml")
            );

            Stage mainStage = new Stage();
            mainStage.setTitle("Bill Payment Service");
            mainStage.setScene(new Scene(loader.load(), 500, 450));
            mainStage.show();

            Stage loginStage =
                    (Stage) loginField.getScene().getWindow();

            loginStage.close();

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Could not sign in."
            );
        }
    }

    @FXML
    private void openRegistration() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ipz/bills/register-view.fxml")
            );

            Stage stage = new Stage();
            stage.setTitle("Registration");
            stage.setScene(new Scene(loader.load(), 500, 450));
            stage.show();

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Could not open the registration window."
            );
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}