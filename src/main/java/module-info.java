module com.company.javafxtutorial {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires com.jfoenix;
    requires java.sql;

    opens GUI to javafx.fxml;
    exports GUI;
    exports FormControllers;
    opens FormControllers to javafx.fxml;
    opens Classes to javafx.base;
}