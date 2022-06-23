package com.project.changzhzfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class creditCardController implements Initializable {
    @FXML
    private TextField customerID, cardNum, nameOnCard, pickMonth,pickYear, cvv;

    @FXML
    private ChoiceBox cardType;
    ObservableList<String> cardTypeList = FXCollections.
            observableArrayList("Visa","Mastercard","American Express","Union Pay");

    @FXML
    private Label pleaseEnter, pleaseTryNew, cardVerify, monthVerify, cvvVerify, successDisplay;

    @FXML
    private Button backBtn, checkExistBtn, checkInfoBtn, placeOrderBtn;

    /**
     * Pre-load all card types to ChoiceBox
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set default value
        cardType.setValue("Visa");
        cardType.setItems(cardTypeList);
    }

    /**
     * Load shopping list page and go back to shopping list
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backBtnClick(ActionEvent event) throws IOException {
        Parent reportsParent = FXMLLoader.load(getClass().getResource("customerBuy.fxml"));
        Scene reportParent = new Scene(reportsParent);
        Stage stockTakeStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stockTakeStage.hide();
        stockTakeStage.setScene(reportParent);
        stockTakeStage.show();
    }

    /**
     * This ActionEvent handler reads from database and helps to check whether a customer exist in the database.
     * Show warning if doesn't exist.
     *
     * @param event
     */
    @FXML
    protected void ifCustomerExist(ActionEvent event){
        //Clean up all the text in fields
        cardNum.setText("");
        cardType.setValue("Visa");
        nameOnCard.setText("");
        pickYear.setText("");
        pickMonth.setText("");
        cvv.setText("");
        pleaseTryNew.setStyle("-fx-opacity: 0");

        //Get ID from customer who is calling.
        String currentCustomer = customerID.getText();

        //Show "please enter" when CustomerID field is empty
        if(currentCustomer.equals("")){
            pleaseEnter.setStyle("-fx-opacity: 1");
        } else {
            pleaseEnter.setStyle("-fx-opacity: 0");
        }

        //Store all customer IDs to see if credit card info exists
        ObservableList<String> customerIDData = FXCollections.observableArrayList();

        //Store old customer info
        ObservableList<Customer> oldCusInfo = FXCollections.observableArrayList();

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
            statement = connection.createStatement();
            ResultSet resultSet;

            //Just get customers' IDs from database
            resultSet = statement.executeQuery("SELECT ID FROM CustomerList");
            String tempID;

            //Read and store ID to list
            while(resultSet.next()){
                tempID=resultSet.getString(1);
                customerIDData.add(tempID);
            }

            //Get all information for the costumer whose ID is in the customerID TextField
            //If we have that customer in our database:
            if(customerIDData.contains(currentCustomer)){
                resultSet = statement.executeQuery("SELECT * FROM CustomerList WHERE ID='"+currentCustomer+"'");
                int age;
                String id, gender,cardType, cardNum, cardHolder,expireDate, cvv;

                //Load, read, and store inputs from database
                while(resultSet.next()){
                    id=resultSet.getString(1);
                    gender=resultSet.getString(5);
                    age=resultSet.getInt(6);
                    cardType=resultSet.getString(7);
                    cardNum=resultSet.getString(8);
                    cardHolder=resultSet.getString(9);
                    expireDate=resultSet.getString(10);
                    cvv=resultSet.getString(11);

                    oldCusInfo.add(new Customer(id,gender,age,cardType, cardNum, cardHolder,expireDate, cvv));
                }
                resultSet.close();
            //If we don't have that customer in our database: (show warning and do no operation)
            } else {
                pleaseTryNew.setStyle("-fx-opacity: 1");
                customerID.setText("");
            }

            //Close the connection to DB
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("SQLException: No information found.");
        }

        //Covert date as a whole to a month number and year number to fill out pickMont and pickYear TextField
        String expireTotal = oldCusInfo.get(0).getExpireDate();
        String expireMonth = expireTotal.substring(0,2);
        String expireYear = expireTotal.substring(3);

        //Fill out all TextField if we have information
        customerID.setText(oldCusInfo.get(0).getId());
        cardNum.setText(oldCusInfo.get(0).getCardNum());
        nameOnCard.setText(oldCusInfo.get(0).getCardHolder());
        cardType.setValue(oldCusInfo.get(0).getCardType());
        pickMonth.setText(expireMonth);
        pickYear.setText(expireYear);
        cvv.setText(oldCusInfo.get(0).getCvv());

    }

    /**
     * This ActionEvent handler checks whether credit card number is 16-digits and cvv is 3-digits,
     * both must not have characters other than numbers
     *
     * @param event
     */
    @FXML
    protected void checkInfoBtnClick(ActionEvent event) {
        pleaseTryNew.setStyle("-fx-opacity: 0");

        //Disable "Place Order" because nothing is entered/ an error just happened.
        placeOrderBtn.setDisable(true);
        //Hide warnings
        cardVerify.setStyle("-fx-opacity: 0");
        monthVerify.setStyle("-fx-opacity: 0");
        cvvVerify.setStyle("-fx-opacity: 0");

        //See whether the car numbers and cvv are formed by numbers. Continue disabling "Place Order" button if no.
        try{
            Long.parseLong(cardNum.getText());
            try{
                Integer.parseInt(cvv.getText());
                placeOrderBtn.setDisable(false);
            //If cvv has character other than numbers: (show warning)
            } catch (RuntimeException e){
                cvvVerify.setStyle("-fx-opacity: 1");
                cvv.setText("");
                placeOrderBtn.setDisable(true);
                System.out.println("CVV Error");
            }
        //If card number has character other than numbers: (show warning)
        } catch (RuntimeException e){
            cardVerify.setStyle("-fx-opacity: 1");
            cardNum.setText("");
            placeOrderBtn.setDisable(true);
            System.out.println("Card Number Error");
        }

        //Check length. Continue disabling "Place Order" button if not match.
        if (cardNum.getText().length()!=16){
            cardVerify.setStyle("-fx-opacity: 1");
            cardNum.setText("");
            placeOrderBtn.setDisable(true);
        } else {
            if (cvv.getText().length()!=3){
                cvvVerify.setStyle("-fx-opacity: 1");
                cvv.setText("");
                placeOrderBtn.setDisable(true);
            } else {
                if (!pickMonth.getText().equals("1")&&
                        !pickMonth.getText().equals("2")&&
                        !pickMonth.getText().equals("3")&&
                        !pickMonth.getText().equals("4")&&
                        !pickMonth.getText().equals("5")&&
                        !pickMonth.getText().equals("6")&&
                        !pickMonth.getText().equals("7")&&
                        !pickMonth.getText().equals("8")&&
                        !pickMonth.getText().equals("9")&&
                        !pickMonth.getText().equals("10")&&
                        !pickMonth.getText().equals("11")&&
                        !pickMonth.getText().equals("12")&&
                        !pickMonth.getText().equals("01")&&
                        !pickMonth.getText().equals("02")&&
                        !pickMonth.getText().equals("03")&&
                        !pickMonth.getText().equals("04")&&
                        !pickMonth.getText().equals("05")&&
                        !pickMonth.getText().equals("06")&&
                        !pickMonth.getText().equals("07")&&
                        !pickMonth.getText().equals("08")&&
                        !pickMonth.getText().equals("09")){
                    monthVerify.setStyle("-fx-opacity: 1");
                    placeOrderBtn.setDisable(true);
                } else {
                    placeOrderBtn.setDisable(false);}
            }
        }
    }

    /**
     * This ActionEvent handler helps to:
     * 1. attach shoppingCart file in database with the current CustomerID
     * 2. store all data to TransactionList (transaction history database)
     *
     * @param event
     */
    @FXML
    protected void placeOrderBtnClick(ActionEvent event){
        //Collect information from customer who is calling.
        String currentCustomer = customerID.getText();
        String currentCardNum = cardNum.getText();
        String currentCardHolder = nameOnCard.getText();
        String currentCardType = cardType.getValue().toString();
        String currentExMonth = pickMonth.getText();
        String currentExYear = pickYear.getText();
        String currentCVV = cvv.getText();

        //Integrate currentExMonth and currentExYear
        String currentExDate = currentExMonth+"/"+currentExYear;

        //Get today's date.
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        String today = dateFormatter.format(new Date());

        //Get a standardized transaction ID
        SimpleDateFormat idFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String deal = idFormatter.format(new Date());

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
            statement = connection.createStatement();

            //Treat the following SQLite statements as a whole
            connection.setAutoCommit(false);
            //Update current customer's information
            statement.execute("UPDATE CustomerList SET CCType='"+currentCardType+"', CCNum='"+currentCardNum+"',"
                            +" CCName='"+currentCardHolder+"', CCExpireDate='"+currentExDate+"',"
                            +"CVV='"+currentCVV+"'\n"
                            +"WHERE ID='"+currentCustomer+"';");

            //Make all items in shopping cart belong to this customer
            statement.execute("UPDATE ShoppingCartCSV SET CustomerID='"+currentCustomer+"';");

            //Transplant information from shopping cart to a temporary transaction history list
            statement.execute("INSERT INTO CurrentTransaction (ItemID,Category,Color,ColorCode,Size,DealPrice," +
                    "Description,CustomerID,Gender,Age,CCType,CCNum,CCName,CCExpireDate,CVV)\n"
              + "SELECT s.ID,Category,Color,ColorCode,Size,DealPrice,Description,c.ID,Gender,Age,CCType,CCNum,CCName,CCExpireDate,CVV\n"
              + "FROM ShoppingCartCSV s\n"
              + "INNER JOIN CustomerList c ON s.CustomerID = c.ID");

            //Fill out today's date and create a transaction number
            // (formatted by Year+Month+Day+Hour+Minute+Second, so that they never get duplicated)
            statement.execute("UPDATE CurrentTransaction SET Date = '"+today+"',"
                    +"TransactionID = '"+currentCustomer+deal+"' WHERE CustomerID='"+currentCustomer+"';");

            //Transplant all information from temporary list to the general transaction history table
            statement.execute(
              "INSERT INTO TransactionList (TransactionID, Date,ItemID,Category,Color,ColorCode,Size,DealPrice,Description,CustomerID,Gender,Age,CCType,CCNum,CCName,CCExpireDate,CVV)\n"
                  + "SELECT *\n"
                  + "FROM CurrentTransaction;");

            //Clean up temporary transaction table and shopping cart table
            statement.execute("DELETE FROM CurrentTransaction;");
            statement.execute("DELETE FROM ShoppingCartCSV");
            connection.commit();

            statement.close();
            connection.close();

            //Show "Success!"
            successDisplay.setStyle("-fx-opacity: 1");

            //Clean up TextFields in case mis-operation
            customerID.setText("");
            cardNum.setText("");
            nameOnCard.setText("");
            cardType.setValue("");
            pickMonth.setText("");
            pickYear.setText("");
            cvv.setText("");

        } catch (SQLException e) {
            System.out.println("SQLException: Cannot read from database");
        }
    }

}
