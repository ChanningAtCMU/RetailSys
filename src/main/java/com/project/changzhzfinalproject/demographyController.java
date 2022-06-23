package com.project.changzhzfinalproject;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.PolicySpi;
import java.sql.*;
import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.ResourceBundle;

public class demographyController implements Initializable {
    @FXML
    private Button backBtn;

    //The following to Panes will illustrate two PieCharts that show gender/credit card type distributions
    @FXML
    private Pane genderPane, cardTypePane;
    //Store customers' gender data
    ObservableList<Customer> genderData = FXCollections.observableArrayList();
    //Initialize numbers for the three genders (male, female, non-binary)
    int[] genderAmountArr = {0,0,0};
    ObservableList<CreditCard> cardTypeData = FXCollections.observableArrayList();
    ArrayList<Integer> cardTypeAmountArr = new ArrayList<>();

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
     * Initialize the PieCharts when we show the page.
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

            //Gender Pie Chart
            //Select gender data and count how many are there for each gender
            resultSet =
                statement.executeQuery("SELECT Gender, COUNT(*) FROM CustomerList GROUP BY Gender;'");
                String gender;
                int amount;

                int three = 0;

                while(resultSet.next()){
                    gender = resultSet.getString(1);
                    amount = resultSet.getInt(2);

                    genderData.add(new Customer(gender));
                    genderAmountArr[three]=amount;
                    three++;
                }

            //Card Type Pie Chart
            //Select card type data and count how many are there for each card type
            resultSet = statement.executeQuery("SELECT CCType, COUNT(*) FROM CustomerList" +
                    " WHERE CCType IS NOT NULL GROUP BY CCType;");
            String CCType;
            int cardTypeAmount;

            //Read and store data lin-by-line
            while(resultSet.next()){
                CCType = resultSet.getString(1);
                cardTypeAmount = resultSet.getInt(2);

                cardTypeData.add(new CreditCard(CCType));
                cardTypeAmountArr.add(cardTypeAmount);
            }

            //Close the retrieving process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("A SQLException occurs");
        }

        //Gender date pie chart
        //Clean the chart first (just in case)
        genderPane.getChildren().clear();
        ObservableList<PieChart.Data> genderPieData = FXCollections.observableArrayList();
        //Store all gender with a corresponding value. In case there will be more genders added,
        // I make it a loop to read all genders.
        for(int i = 0; i<genderAmountArr.length; i++){
            genderPieData.add(new PieChart.Data(genderData.get(i).getGender(),genderAmountArr[i]));
        }
        //Lambda expression helps to bind value with the labels
        genderPieData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(),": ",
                data.pieValueProperty().intValue())));
        //Load data to the PieChart
        PieChart genderChart = new PieChart(genderPieData);
        genderChart.setTitle("Gender Distribution");
        //Show PieChart as a children of the pane
        genderPane.getChildren().add(genderChart);


        //Card type data pie chart, same process as above
        cardTypePane.getChildren().clear();
        ObservableList<PieChart.Data> cardTypePieData = FXCollections.observableArrayList();
        for(int i = 0; i<cardTypeAmountArr.size(); i++){
            cardTypePieData.add(new PieChart.Data(cardTypeData.get(i).getCardType(),cardTypeAmountArr.get(i)));
        }
        cardTypePieData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(),": ",
                data.pieValueProperty().intValue())));
        PieChart cardTypeChart = new PieChart(cardTypePieData);
        cardTypeChart.setTitle("Credit Card Type Distribution");
        cardTypePane.getChildren().add(cardTypeChart);
    }
}
