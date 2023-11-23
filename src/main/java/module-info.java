module net.novahc.smanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires webcam.capture;
    requires io.github.eduramiba.webcamcapture;
    requires org.slf4j;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens net.novahc.smanagement to javafx.fxml;
    exports net.novahc.smanagement;
}