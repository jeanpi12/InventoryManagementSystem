module atiles.c482project {
    requires javafx.controls;
    requires javafx.fxml;


    opens atiles.c482project to javafx.fxml;
    exports atiles.c482project;
    exports Controller;
    opens Controller to javafx.fxml;
    opens Model to javafx.base;
}