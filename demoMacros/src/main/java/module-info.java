module com.example.demomacros {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jdk.jsobject;
    requires java.sql;
    requires java.desktop;
    requires org.json;

    opens com.example.demomacros to javafx.fxml;
    exports com.example.demomacros;
}