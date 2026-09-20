package com.ipz.bills;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryController {

    @FXML
    private TableView<Payment> paymentTable;

    @FXML
    private TableColumn<Payment, String> typeColumn;

    @FXML
    private TableColumn<Payment, String> detailsColumn;

    @FXML
    private TableColumn<Payment, Double> amountColumn;

    @FXML
    public void initialize() {

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );

        detailsColumn.setCellValueFactory(
                new PropertyValueFactory<>("details")
        );

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        loadHistory();
    }

    private void loadHistory() {

        try {
            String response = NetworkClient.send(
                    "HISTORY|" + Session.getUsername()
            );

            ObservableList<Payment> payments =
                    FXCollections.observableArrayList();

            if (response != null
                    && response.startsWith("HISTORY|")) {

                String data = response.substring(8);

                if (!data.isBlank()) {

                    String[] records = data.split(";");

                    for (String record : records) {

                        String[] fields = record.split(",");

                        if (fields.length == 3) {

                            payments.add(
                                    new Payment(
                                            fields[0],
                                            fields[1],
                                            Double.parseDouble(fields[2])
                                    )
                            );
                        }
                    }
                }
            }

            paymentTable.setItems(payments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}