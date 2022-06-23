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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Iterator;
import java.util.ResourceBundle;

public class customerBuyController implements Initializable {
    @FXML
    private TableView regularTable;

    @FXML
    private TableColumn<Item,String> idCol;

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

    ObservableList<Item> regularData = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox regularChoice;
    ObservableList<String> regularItemList = FXCollections.observableArrayList();

    @FXML
    private Button addToCartBtn, placeOrderBtn;

    @FXML
    private Label successDisplay;

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
     * Initialize the page by showing all items in stock
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
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();

            //Get all items that has the status "in stock"
            resultSet =
                    statement.executeQuery("SELECT * FROM StoresList WHERE Status = 'In Stock'");
            String itemID, category, color, colorCode, size, description;
            double price;

            //Read and load all input from database
            while(resultSet.next()){
                itemID = resultSet.getString(1);
                category = resultSet.getString(2);
                color = resultSet.getString(3);
                colorCode = resultSet.getString(4);
                size = resultSet.getString(5);
                price = resultSet.getDouble(6);
                description = resultSet.getString(8);

                regularData.add(new Item(itemID,category,color,colorCode,size,price,description));
                regularItemList.add(itemID);
            }

            //Fill out regularChoice ChoiceBox with all "in stock" items' IDs
            regularTable.setItems(regularData);
            regularChoice.setItems(regularItemList);

            //Close the loading process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Fill out the columns by the data retrieved above
        idCol.setCellValueFactory(new PropertyValueFactory<Item,String>("id"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        colorCol.setCellValueFactory(new PropertyValueFactory<Item,String>("color"));
        colorCodeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("colorCode"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("price"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
    }

    /**
     *
     */
    @FXML
    protected void addToCartBtnClick(){
        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String selected = regularChoice.getValue().toString();

        try{
            Statement statement;
            statement = connection.createStatement();

            //Statement.execute --> return an int (how many rows being edited)

            //Read from storage table and copy information for the selected item to the shopping cart
            connection.setAutoCommit(false);
            statement.execute("INSERT INTO ShoppingCartCSV (ID, Category, Color, ColorCode, Size, DealPrice, Description)\n"
                    + "SELECT ID, Category, Color, ColorCode, Size, Price, Description FROM StoresList\n"
                    + "WHERE ID = '"+selected+"' AND Status = 'In Stock';\n");

            //Update storage table and change the item's status from "In Stock" to "Sold"
            statement.execute("UPDATE StoresList SET Status = 'Sold' WHERE ID = '"+selected+"';");
            connection.commit();

            statement.close();
            connection.close();

            successDisplay.setStyle("-fx-opacity: 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Remove the itemID from the ChoiceBox after it has been put into shopping cart
        for (Iterator<String> i = regularItemList.iterator(); i.hasNext();) {
            String element = i.next();
            if (element.equals(selected))
                i.remove();
        }
        regularChoice.setItems(regularItemList);
        regularChoice.setValue("");
    }


    /**
     * To place order -> go to payment page
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void placeOrderBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("creditCard.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage stockTakeStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stockTakeStage.hide();
        stockTakeStage.setScene(reportParent);
        stockTakeStage.show();
    }
}
//connect.setAutoCommit(false);
//connect.commit()
