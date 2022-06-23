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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;

import java.io.IOException;
import java.util.ResourceBundle;

public class allStorageController implements Initializable {
    @FXML
    private TableView storageTable;

    @FXML
    private TableColumn<Item,String> itemIDCol;

    @FXML
    private TableColumn<Item,String> categoryCol;

    @FXML
    private TableColumn<Item,String> colorCol;

    @FXML
    private TableColumn<Item,String> colorCodeCol;

    @FXML
    private TableColumn<Item,String> sizeCol;

    @FXML
    private TableColumn<Item,Double> priceCol;

    @FXML
    private TableColumn<Item,String> descriptionCol;

    //Store all Item Object from ResultSet
    ObservableList<Item> itemData = FXCollections.observableArrayList();

    /**
     * When click the back button, go back to the homepage.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage stockTakeStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stockTakeStage.hide();
        stockTakeStage.setScene(reportParent);
        stockTakeStage.show();
    }

    /**
     * Pre-load all storage and doesn't allow any change on this page
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Class not found");
        }

        //Build Connection Object and get directory
        Connection connection = null;
        try {
         connection =
            DriverManager.getConnection(
              "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            System.out.println("Database not found");
        }

        try{
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();

            //Get entire table from database
            resultSet = statement.executeQuery("SELECT * FROM StoresList WHERE Status='In Stock'");
            String itemID, category, color, colorCode, size, description;
            double price;

            //Read data line-by-line
            while(resultSet.next()){
                itemID = resultSet.getString(1);
                category = resultSet.getString(2);
                color = resultSet.getString(3);
                colorCode = resultSet.getString(4);
                size = resultSet.getString(5);
                price = resultSet.getDouble(6);
                description = resultSet.getString(8);

                itemData.add(new Item(itemID,category,color,colorCode,size,price,description));
            }

            //Load objects to the table
            storageTable.setItems(itemData);

            //Close the process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Load data to columns
        itemIDCol.setCellValueFactory(new PropertyValueFactory<Item,String>("id"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        colorCol.setCellValueFactory(new PropertyValueFactory<Item,String>("color"));
        colorCodeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("colorCode"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("price"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
    }
}
