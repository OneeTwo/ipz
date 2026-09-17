package com.ipz.bills;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister() {
        try {
            String username = loginField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isBlank()
                    || password.isBlank()
                    || confirmPassword.isBlank()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Fill in all fields."
                );
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Passwords do not match."
                );
                return;
            }

            boolean registered =
                    UserStore.register(username, password);

            if (!registered) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "A user with this username already exists."
                );
                return;
            }

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Registration completed successfully."
            );

            Stage stage =
                    (Stage) loginField.getScene().getWindow();

            stage.close();

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Registration failed."
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