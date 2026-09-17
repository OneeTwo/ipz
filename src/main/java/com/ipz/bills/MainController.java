package com.ipz.bills;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField detailsField;

    @FXML
    private TextField amountField;

    @FXML
    private Button payButton;

    public static final ObservableList<Payment> payments =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        typeCombo.getItems().addAll(
                "Mobile Phone",
                "Electricity",
                "Gas",
                "Water",
                "Internet"
        );

        /*
         * Data Binding:
         * Pay button is disabled until all required fields
         * contain data.
         */
        payButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> typeCombo.getValue() == null
                                || detailsField.getText().isBlank()
                                || amountField.getText().isBlank(),
                        typeCombo.valueProperty(),
                        detailsField.textProperty(),
                        amountField.textProperty()
                )
        );
    }

    @FXML
    private void handlePayment() {
        try {
            String type = typeCombo.getValue();
            String details = detailsField.getText().trim();

            double amount = Double.parseDouble(
                    amountField.getText()
                            .trim()
                            .replace(",", ".")
            );

            if (amount <= 0) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Amount must be greater than 0."
                );
                return;
            }

            Payment payment =
                    new Payment(type, details, amount);

            payments.add(payment);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Payment completed successfully."
            );

            typeCombo.setValue(null);
            detailsField.clear();
            amountField.clear();

        } catch (NumberFormatException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Enter a valid amount."
            );

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Payment failed."
            );
        }
    }

    @FXML
    private void openHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ipz/bills/history-view.fxml")
            );

            Stage stage = new Stage();
            stage.setTitle("Payment History");
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.show();

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Could not open payment history."
            );
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ipz/bills/login-view.fxml")
            );

            Stage loginStage = new Stage();
            loginStage.setTitle("Bill Payment Service");
            loginStage.setScene(
                    new Scene(loader.load(), 500, 400)
            );
            loginStage.show();

            Stage currentStage =
                    (Stage) typeCombo.getScene().getWindow();

            currentStage.close();

        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Could not log out."
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