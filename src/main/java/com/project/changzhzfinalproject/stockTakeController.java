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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class stockTakeController implements Initializable {

    @FXML
    private ChoiceBox categoryBox;
    ObservableList<String> categoryList = FXCollections.
            observableArrayList("Shirt","Trouser","Jacket");

    @FXML
    private ChoiceBox colorBox;
    ObservableList<String> colorList = FXCollections.
            observableArrayList("Black","Blue","Red","White");
    @FXML
    private ChoiceBox sizeBox;
    ObservableList<String> sizeList = FXCollections.
            observableArrayList("S","M","L","XL","XXL");

    @FXML
    private Label numAvailable;
    int totalNum;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label notFoundWarning;

    public stockTakeController() throws FileNotFoundException {
    }

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
     * Get quantity for the selected Item
     * Item is specified by category, color, and size
     *
     * @return Quantity of the target item
     */
    @FXML
    protected int getQuantity(){
        String targetCategory = categoryBox.getValue().toString();
        String targetColor = colorBox.getValue().toString();
        String targetSize = sizeBox.getValue().toString();

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

            //Load and read all information for items that satisfy the conditions in the ChoiceBoxes
            resultSet =
                    statement.executeQuery(
                            "SELECT COUNT (*) \n"
                                    + "FROM StoresList \n"
                                    + "WHERE Category IN ('"+targetCategory
                                    +"') AND Color IN ('"+targetColor+"') AND Size IN ('"+targetSize+"')" +
                                    "AND Status = 'In Stock';");

            //Load, read, and store the result
            while(resultSet.next()){
                totalNum = resultSet.getInt(1);
            }

            //Close retrieving process
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to retrieve data");
        }
        return totalNum;
    }

    /**
     * Give choices to ChoiceBoxes when we initialize the page
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        categoryBox.setValue("");
        categoryBox.setItems(categoryList);

        colorBox.setValue("");
        colorBox.setItems(colorList);

        sizeBox.setValue("");
        sizeBox.setItems(sizeList);
    }

    //The next block of code loads all the cloth/NotFound pictures.
    @FXML
    private ImageView clothPic;

    //Load image from local directory
    Image blackJacket =
      new Image(
          new FileInputStream(
              "src/main/resources/Images/blackJacket.jpeg"));
    Image whiteJacket = new Image(
            new FileInputStream(
                    "src/main/resources/Images/whiteJacket.jpg"));
    Image redJacket = new Image(
            new FileInputStream(
                    "src/main/resources/Images/redJacket.jpg"));
    Image blackShirt = new Image(
            new FileInputStream(
                    "src/main/resources/Images/blackShirt.jpg"));
    Image whiteShirt = new Image(
            new FileInputStream(
                    "src/main/resources/Images/whiteShirt.jpeg"));
    Image blueShirt = new Image(
            new FileInputStream(
                    "src/main/resources/Images/blueShirt.jpeg"));
    Image redShirt = new Image(
            new FileInputStream(
                    "src/main/resources/Images/redShirt.jpg"));
    Image blackTrouser = new Image(
            new FileInputStream(
                    "src/main/resources/Images/blackTrouser.jpg"));
    Image blueTrouser = new Image(
            new FileInputStream(
                    "src/main/resources/Images/blueTrouser.jpg"));
    Image whiteTrouser = new Image(
            new FileInputStream(
                    "src/main/resources/Images/whiteTrouser.jpeg"));
    Image notFound =
            new Image(
                    new FileInputStream(
                            "src/main/resources/Images/notFound.jpg"));

    /**
     * When click the button, get input from the three ChoiceBox (Category, Color, Size)
     * and count its amount form database.
     *
     * @param e
     *
     * @return Change in numAvailable
     * @return Change to the corresponding picture
     */
    @FXML
    protected void checkingBtnClick(ActionEvent e){
        //If there is an empty ChoiceBox
        if(categoryBox.getValue().toString().equals("")||
                colorBox.getValue().toString().equals("")||
                sizeBox.getValue().toString().equals("")){
            clothPic.setImage(notFound);
            //Show the hidden warning label
            notFoundWarning.setStyle("-fx-opacity: 1");
            //Show general description for each type of item,
            // and show "Missing elements" when one or of the ChoiceBox(es) is empty
            descriptionLabel.setText("Missing necessary elements.");
        //Otherwise, if we have the following conditions as input, change the picture, count and show the total number
        } else if (categoryBox.getValue().toString().equals("Shirt")&&
                colorBox.getValue().toString().equals("White")){
            clothPic.setImage(whiteShirt);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Classic never fade.");
        } else if (categoryBox.getValue().toString().equals("Shirt")&&
                colorBox.getValue().toString().equals("Black")){
            clothPic.setImage(blackShirt);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Il più alla moda");
        } else if (categoryBox.getValue().toString().equals("Shirt")&&
                colorBox.getValue().toString().equals("Blue")){
            clothPic.setImage(blueShirt);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Popular blue shirt.");
        } else if (categoryBox.getValue().toString().equals("Shirt")&&
                colorBox.getValue().toString().equals("Red")){
            clothPic.setImage(redShirt);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Feliz La Vida!");
        } else if (categoryBox.getValue().toString().equals("Jacket")&&
                colorBox.getValue().toString().equals("Black")){
            clothPic.setImage(blackJacket);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Looks COOOOOOOOOOOOOOOL");
        } else if (categoryBox.getValue().toString().equals("Jacket")&&
                colorBox.getValue().toString().equals("White")){
            clothPic.setImage(whiteJacket);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Looks FASHIONABLE");
        } else if (categoryBox.getValue().toString().equals("Jacket")&&
                colorBox.getValue().toString().equals("Red")){
            clothPic.setImage(redJacket);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Make You Different");
        } else if (categoryBox.getValue().toString().equals("Trouser")&&
                colorBox.getValue().toString().equals("Black")){
            clothPic.setImage(blackTrouser);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Hèmes dies for this.");
        } else if (categoryBox.getValue().toString().equals("Trouser")&&
                colorBox.getValue().toString().equals("Blue")){
            clothPic.setImage(blueTrouser);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Not that popular.");
        } else if (categoryBox.getValue().toString().equals("Trouser")&&
                colorBox.getValue().toString().equals("White")){
            clothPic.setImage(whiteTrouser);
            numAvailable.setText(String.valueOf(getQuantity()));
            notFoundWarning.setStyle("-fx-opacity: 0");
            descriptionLabel.setText("Pantalones Favoritos");
        } else {
            clothPic.setImage(notFound);
            numAvailable.setText("0");
            //If quantity is 0, show "Not in stock"
            descriptionLabel.setText("Not in stock.");
        }
    }
}
