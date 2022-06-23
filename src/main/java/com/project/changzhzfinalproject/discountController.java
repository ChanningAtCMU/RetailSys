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

public class discountController implements Initializable {
    @FXML
    private TableView discountTable;

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
    private TableColumn<Item,Double> discountCol;

    @FXML
    private TableColumn<Item,String> descriptionCol;

    ObservableList<Item> discountData = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox discountChoice;
    ObservableList<String> discountItemList = FXCollections.observableArrayList();

    @FXML
    private Button emailBtnClick, addToCartBtn, placeOrderBtn;

    @FXML
    private Label emailSuccess, successDisplay;

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
     * Initialize the page by loading items from storage table with their discount price
     * The items must have status of "In Stock"
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
            System.out.println("Database Not Found");
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            System.out.println("Failed to connect database");
        }

        try{
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();

        //Randomly select 10 items from the storage table
        resultSet =
          statement.executeQuery("SELECT * FROM StoresList WHERE Status = 'In Stock' ORDER BY RANDOM() LIMIT 10;");
            String itemID, category, color, colorCode, size, description;
            double discountPrice,price;

            //Load, read, and store data
            while(resultSet.next()){
                itemID = resultSet.getString(1);
                category = resultSet.getString(2);
                color = resultSet.getString(3);
                colorCode = resultSet.getString(4);
                size = resultSet.getString(5);
                price = resultSet.getDouble(6);
                discountPrice = resultSet.getDouble(7);
                description = resultSet.getString(8);

                discountData.add(new Item(itemID,category,color,colorCode,size,price,discountPrice,description));
                discountItemList.add(itemID);
            }

            //Load data to table and fill out ChoiceBox with ItemIDs
            discountTable.setItems(discountData);
            discountChoice.setItems(discountItemList);

            //Close all process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Having issue with SQL statements.");
        }

        //Fill out all columns
        idCol.setCellValueFactory(new PropertyValueFactory<Item,String>("id"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        colorCol.setCellValueFactory(new PropertyValueFactory<Item,String>("color"));
        colorCodeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("colorCode"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("price"));
        discountCol.setCellValueFactory(new PropertyValueFactory<Item,Double>("discountPrice"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Item,String>("description"));
    }

    /**
     * Add the item that its ID is in the ChoiceBox to shoppingCart table
     *
     * @param event
     */
    @FXML
    protected void addToCartBtnClick(ActionEvent event){
        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("DB Not Found");
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Get itemID and read as a string
        String selected = discountChoice.getValue().toString();

        try{
            Statement statement;
            statement = connection.createStatement();

            connection.setAutoCommit(false);
            //Statement.execute --> return an int (how many rows being edited)
            //Read information for the selected item with the current itemID in teh ChoiceBox,
            //and paste the info to the shoppingCart table
            statement.execute("INSERT INTO ShoppingCartCSV (ID, Category, Color, ColorCode, Size, DealPrice, Description)\n"
                        + "SELECT ID, Category, Color, ColorCode, Size, DiscountPrice, Description FROM StoresList\n"
                        + "WHERE ID = '"+selected+"' AND Status = 'In Stock';\n");

            //Update the storageList by changing the items' status to "Sold"
            statement.execute("UPDATE StoresList SET Status = 'Sold' WHERE ID = '"+selected+"';");
            connection.commit();

            //Close process
            statement.close();
            connection.close();

            //Show "Success!" for a valid addToCart event
            successDisplay.setStyle("-fx-opacity: 1");

        } catch (SQLException e) {
            System.out.println("Having issue with SQL statements.");
        }

        //Remove itemID from ChoiceBox after being added to shopping cart
        for (Iterator<String> i = discountItemList.iterator(); i.hasNext();) {
            String element = i.next();
            if (element.equals(selected))
                i.remove();
        }
        //Refresh the ChoiceBox and set no default value
        discountChoice.setItems(discountItemList);
        discountChoice.setValue("");
    }

    /**
     * When user is ready, go to the check-out page
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

    /**
     * A beta button for sending email
     *
     * @param event
     */
    @FXML
    protected void emailBtnClick(ActionEvent event){
        //Show "Success" when an email is sent
        emailSuccess.setStyle("-fx-opacity: 1");
    }
}

//connect.setAutoCommit(false);
//connect.commit()
