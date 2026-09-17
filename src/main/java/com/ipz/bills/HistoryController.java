package com.ipz.bills;

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

        paymentTable.setItems(MainController.payments);
    }
}