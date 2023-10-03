module net.novahc.smanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens net.novahc.smanagement to javafx.fxml;
    exports net.novahc.smanagement;
}