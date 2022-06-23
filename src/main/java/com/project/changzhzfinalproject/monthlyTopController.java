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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class monthlyTopController implements Initializable {
    @FXML
    private Button backBtn, monthBtn;

    @FXML
    private BarChart shirtChart, trouserChart, jacketChart;

    @FXML
    private ChoiceBox monthChoice;
    //All months as choices
    ObservableList<Integer> monthList = FXCollections.
            observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);


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
     * Set values for ChoiceBox before any other actions/events.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set up the monthChoice ChoiceBox
        monthChoice.setValue(10);
        monthChoice.setItems(monthList);
    }

    @FXML
    protected void viewBtnClick(ActionEvent event){
        //Clean up all the charts
        shirtChart.getData().clear();
        jacketChart.getData().clear();
        trouserChart.getData().clear();

        String colorName;
        int colorAmount;

        //Create ArrayLists to store necessary keys and values for charts
        //Store colors for shirt
        ArrayList<String> colorArray = new ArrayList<>();
        //Store amount of shirts for each of the top 3 colors
        ArrayList<Integer> amountArray = new ArrayList<>();
        //Store color and amount data for the charts
        XYChart.Series<String, Number> color1 = new XYChart.Series<>();
        XYChart.Series<String, Number> color2 = new XYChart.Series<>();
        XYChart.Series<String, Number> color3 = new XYChart.Series<>();

        //Store data for trousers
        ArrayList<String> trouserColorArray = new ArrayList<>();
        ArrayList<Integer> trouserAmountArray = new ArrayList<>();
        XYChart.Series<String, Number> Tcolor1 = new XYChart.Series<>();
        XYChart.Series<String, Number> Tcolor2 = new XYChart.Series<>();
        XYChart.Series<String, Number> Tcolor3 = new XYChart.Series<>();

        //Store data for jackets
        ArrayList<String> jacketColorArray = new ArrayList<>();
        ArrayList<Integer> jacketAmountArray = new ArrayList<>();
        XYChart.Series<String, Number> Jcolor1 = new XYChart.Series<>();
        XYChart.Series<String, Number> Jcolor2 = new XYChart.Series<>();
        XYChart.Series<String, Number> Jcolor3 = new XYChart.Series<>();

        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find JDBC class");
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            System.out.println("SQLException: Failed to connect to database");
        }

        try{
            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;

            //Get top three (if exist) colors for shirt
            resultSet =
                    statement.executeQuery(
                            "SELECT Color, count(Color) AS 'Occurrence'\n"
                                    + "FROM TransactionList\n"
                                    + "WHERE Category = 'Shirt' AND Date LIKE '"+monthChoice.getValue().toString()+"%'\n"
                                    + "GROUP BY Color\n"
                                    + "LIMIT 3;");

            //Load, read, and store data
            while(resultSet.next()){
                colorName=resultSet.getString(1);
                colorAmount=resultSet.getInt(2);

                colorArray.add(colorName);
                amountArray.add(colorAmount);
            }

            //Depend on how many color are there-> if 1, only load 1 set of data; if 2, load 2 sets; etc.
            if (colorArray.size()==1){
                color1.setName(colorArray.get(0));
                color1.getData().add(new XYChart.Data<>("Shirt",amountArray.get(0)));
                shirtChart.getData().addAll(color1);
            } else if (colorArray.size()==2){
                color1.setName(colorArray.get(0));
                color2.setName(colorArray.get(1));
                color1.getData().add(new XYChart.Data<>("Shirt",amountArray.get(0)));
                color2.getData().add(new XYChart.Data<>("Shirt",amountArray.get(1)));
                shirtChart.getData().addAll(color1,color2);
            } else if (colorArray.size()==3){
                color1.setName(colorArray.get(0));
                color2.setName(colorArray.get(1));
                color3.setName(colorArray.get(2));
                color1.getData().add(new XYChart.Data<>("Shirt",amountArray.get(0)));
                color2.getData().add(new XYChart.Data<>("Shirt",amountArray.get(1)));
                color3.getData().add(new XYChart.Data<>("Shirt",amountArray.get(2)));
                shirtChart.getData().addAll(color1,color2,color3);
            }

            //Get top three (if exist) colors for trousers, same following process as above
            resultSet =
                    statement.executeQuery(
                            "SELECT Color, count(Color) AS 'Occurrence'\n"
                                    + "FROM TransactionList\n"
                                    + "WHERE Category = 'Trouser' AND Date LIKE '"+monthChoice.getValue().toString()+"%'\n"
                                    + "GROUP BY Color\n"
                                    + "LIMIT 3;");

            while(resultSet.next()){
                colorName=resultSet.getString(1);
                colorAmount=resultSet.getInt(2);

                trouserColorArray.add(colorName);
                trouserAmountArray.add(colorAmount);
            }

            if (trouserColorArray.size()==1){
                Tcolor1.setName(trouserColorArray.get(0));
                Tcolor1.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(0)));
                trouserChart.getData().addAll(Tcolor1);
            } else if (trouserColorArray.size()==2){
                Tcolor1.setName(trouserColorArray.get(0));
                Tcolor2.setName(trouserColorArray.get(1));
                Tcolor1.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(0)));
                Tcolor2.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(1)));
                trouserChart.getData().addAll(Tcolor1,Tcolor2);
            } else if (trouserColorArray.size()==3){
                Tcolor1.setName(trouserColorArray.get(0));
                Tcolor2.setName(trouserColorArray.get(1));
                Tcolor3.setName(trouserColorArray.get(2));
                Tcolor1.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(0)));
                Tcolor2.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(1)));
                Tcolor3.getData().add(new XYChart.Data<>("Trousers",trouserAmountArray.get(2)));
                trouserChart.getData().addAll(Tcolor1,Tcolor2,Tcolor3);
            }

            //Get top three (if exist) colors for jackets, same following process as above
            resultSet =
                    statement.executeQuery(
                            "SELECT Color, count(Color) AS 'Occurrence'\n"
                                    + "FROM TransactionList\n"
                                    + "WHERE Category = 'Jacket' AND Date LIKE '"+monthChoice.getValue().toString()+"%'\n"
                                    + "GROUP BY Color\n"
                                    + "LIMIT 3;");

            while(resultSet.next()){
                colorName=resultSet.getString(1);
                colorAmount=resultSet.getInt(2);

                jacketColorArray.add(colorName);
                jacketAmountArray.add(colorAmount);
            }

            if (jacketColorArray.size()==1){
                Jcolor1.setName(jacketColorArray.get(0));
                Jcolor1.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(0)));
                jacketChart.getData().addAll(Jcolor1);
            } else if (jacketColorArray.size()==2){
                Jcolor1.setName(jacketColorArray.get(0));
                Jcolor2.setName(jacketColorArray.get(1));
                Jcolor1.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(0)));
                Jcolor2.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(1)));
                jacketChart.getData().addAll(Jcolor1,Jcolor2);
            } else if (jacketColorArray.size()==3){
                Jcolor1.setName(jacketColorArray.get(0));
                Jcolor2.setName(jacketColorArray.get(1));
                Jcolor3.setName(jacketColorArray.get(2));
                Jcolor1.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(0)));
                Jcolor2.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(1)));
                Jcolor3.getData().add(new XYChart.Data<>("Jacket",jacketAmountArray.get(2)));
                jacketChart.getData().addAll(Jcolor1,Jcolor2,Jcolor3);
            }

            //Close the retrieving process
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("A SQL exception occurs.");
        }
    }
}
