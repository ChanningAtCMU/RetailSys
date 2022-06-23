package com.project.changzhzfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    @FXML
    private Button goShoppingBtn;
    @FXML
    private Button checkStorage;
    @FXML
    private Button goToReportBtn;
    @FXML
    private Button discountBtn;

    //THis ChoiceBox directs users to each of the four reports.
    @FXML
    private ChoiceBox reportsChoiceBox;
    ObservableList<String> reportsList = FXCollections.
            observableArrayList("Stocktaking","Quarterly Sales","Monthly Report","Customer Demographics");

    /**
     * Pre-load all options to the ChoiceBox
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportsChoiceBox.setValue("Stocktaking");
        reportsChoiceBox.setItems(reportsList);
    }

    /**
     * Go to regular purchasing page when click the goShoppingBtn Button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void shoppingBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("customerBuy.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage nextStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        nextStage.hide();
        nextStage.setScene(reportParent);
        nextStage.show();
    }

    /**
     * 1. Read from ChoiceBox
     * 2. Go to each of the report by clicking the goToReportBtn Button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void goToReport(ActionEvent event) throws IOException {
        //Read from reportsChoiceBox ChoiceBox
        String reportToView = reportsChoiceBox.getValue().toString();
        Parent reportsParent = null;
        //Load page based on the selected option
        if(reportToView.equals("Stocktaking")){
            reportsParent = FXMLLoader.load(getClass().getResource("stockTake.fxml"));
        } else if (reportToView.equals("Quarterly Sales")){
            reportsParent = FXMLLoader.load(getClass().getResource("quarterlySales.fxml"));
        } else if (reportToView.equals("Monthly Report")){
            reportsParent = FXMLLoader.load(getClass().getResource("monthlyTop.fxml"));
        } else if (reportToView.equals("Customer Demographics")){
            reportsParent = FXMLLoader.load(getClass().getResource("demography.fxml"));
        }
        Scene reportParent = new Scene(reportsParent);
        Stage nextStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        nextStage.hide();
        nextStage.setScene(reportParent);
        nextStage.show();
    }

    /**
     * Go to discounted purchasing page when click the discountBtn Button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void discountBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("discountPage.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage nextStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        nextStage.hide();
        nextStage.setScene(reportParent);
        nextStage.show();
    }

    /**
     * Go to view all storage page when click the checkStorage Button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void checkStorageBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("allStorage.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage nextStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        nextStage.hide();
        nextStage.setScene(reportParent);
        nextStage.show();
    }
}
