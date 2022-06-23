module com.project.changzhzfinalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.project.changzhzfinalproject to javafx.fxml;
    exports com.project.changzhzfinalproject;
}