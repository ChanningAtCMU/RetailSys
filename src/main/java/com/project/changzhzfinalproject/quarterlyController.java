package com.project.changzhzfinalproject;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class quarterlyController implements Initializable {
    @FXML
    private Button backBtn, quarterView, showPie;

    @FXML
    private ChoiceBox quarterChoice;
    ObservableList<String> quarterList = FXCollections.
            observableArrayList("I", "II", "III", "IV");

    @FXML
    private TableView quarterTable;
    ObservableList<Item> quarterData = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Item, String> categoryCol;
    @FXML
    private TableColumn<Item, String> colorCol;
    @FXML
    private TableColumn<Item, String> sizeCol;
    @FXML
    private TableColumn<Item, Integer> amountCol;

    String sqlFragment;

    @FXML
    private Pane quarterPane;
    ObservableList<Double> quarterPieData = FXCollections.observableArrayList();

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
        quarterChoice.setValue("IV");
        quarterChoice.setItems(quarterList);
    }

    /**
     * View top sales products by grouped by quarters
     *
     * @param event
     */
    @FXML
    protected void viewBtnClick(ActionEvent event){
        //Start with an empty table
        quarterTable.getItems().clear();
        quarterData.clear();

        //"I" -> 1st quarter
        //"II" -> 2nd quarter
        //"III" -> 3rd quarter
        //"IV" -> 4th quarter
        //Pre-write corresponding SQL statement fragment
        if (quarterChoice.getValue().equals("I")){
            sqlFragment = "'01%' OR Date LIKE '02%' OR Date LIKE '03%'";
        } else if (quarterChoice.getValue().equals("II")){
            sqlFragment = "'04%' OR Date LIKE '05%' OR Date LIKE '06%'";
        } else if (quarterChoice.getValue().equals("III")){
            sqlFragment = "'07%' OR Date LIKE '08%' OR Date LIKE '09%'";
        } else if (quarterChoice.getValue().equals("IV")){
            sqlFragment = "'10%' OR Date LIKE '11%' OR Date LIKE '12%'";
        } else {
            sqlFragment = "'10%' OR Date LIKE '11%' OR Date LIKE '12%'";
        }

        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot find this JDBC Class");
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
        }

        try{
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();

            //Select and load data only for the quarter selected in the ChoiceBox
            resultSet = statement.executeQuery("SELECT Category, Color, Size, COUNT(*) AS amountSold\n" +
                    "FROM TransactionList\n" +
                    "WHERE Date LIKE "+sqlFragment+"\n" +
                    "GROUP BY Category, Color, Size\n" +
                    "ORDER BY amountSold DESC;");
            String itemCategory, itemColor, itemSize, itemAmount;

            //Load, read, and store the data
            while(resultSet.next()){
                itemCategory = resultSet.getString(1);
                itemColor = resultSet.getString(2);
                itemSize = resultSet.getString(3);
                itemAmount = resultSet.getString(4);

                quarterData.add(new Item(itemCategory, itemColor, itemSize, Integer.parseInt(itemAmount)));
            }

            //Load data to the table
            quarterTable.setItems(quarterData);

            //Close all the loading process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to retrieve data");
        }

        //Load data to columns
        categoryCol.setCellValueFactory(new PropertyValueFactory<Item,String>("category"));
        colorCol.setCellValueFactory(new PropertyValueFactory<Item,String>("color"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<Item,String>("size"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Item,Integer>("amount"));

    }

    /**
     * Click to load data and show the PieChart
     * This PieChart shows the revenue distribution for all four quarters
     * @param event
     */
    @FXML
    protected void showPieClick(ActionEvent event){
        //Connect to database
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find the JDBC Class");
        }

        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection(
                            "jdbc:sqlite:src/main/resources/changzhzFinalDB.db");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }

        try{
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();

        //Gender Pie Chart
        //Load all data, group by every 4 months
        resultSet =
          statement.executeQuery(
              "SELECT SUM(DealPrice) AS QuarterTotal\n"
                  + "FROM TransactionList\n"
                  + "GROUP BY Date LIKE '01%' OR Date LIKE '02%' OR Date LIKE '03%', \n"
                  + "Date LIKE '04%' OR Date LIKE '05%' OR Date LIKE '06%',\n"
                  + "Date LIKE '07%' OR Date LIKE '08%' OR Date LIKE '09%',\n"
                  + "Date LIKE '10%' OR Date LIKE '11%' OR Date LIKE '12%';'");
            double totalRevenue;

            //Load, read, and store data lin-by-line
            while(resultSet.next()){
                totalRevenue = resultSet.getDouble(1);

                quarterPieData.add(totalRevenue);
            }

            //Close retrieving process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to read data");
        }

        //Quarterly revenue date pie chart
        quarterPane.getChildren().clear();
        ObservableList<PieChart.Data> quarterTempData = FXCollections.observableArrayList();
        //Load key-value pairs to ObservableList<PieChart.Data>
        quarterTempData.add(new PieChart.Data("1st Quarter",quarterPieData.get(3)));
        quarterTempData.add(new PieChart.Data("2nd Quarter",quarterPieData.get(2)));
        quarterTempData.add(new PieChart.Data("3rd Quarter",quarterPieData.get(1)));
        quarterTempData.add(new PieChart.Data("4th Quarter",quarterPieData.get(0)));
        //Add value to labels
        quarterTempData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(),": ",
                data.pieValueProperty(),"$")));
        PieChart quarterChart = new PieChart(quarterTempData);
        //Hide legend
        quarterChart.setLegendVisible(false);
        quarterChart.setTitle("Quarterly Revenue");
        //Show PieChart as a children of the Pane
        quarterPane.getChildren().add(quarterChart);
    }

}

