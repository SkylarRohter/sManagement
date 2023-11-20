module net.novahc.smanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;



    opens net.novahc.smanagement to javafx.fxml;
    exports net.novahc.smanagement;
}