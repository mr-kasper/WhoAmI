module org.example.testapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires com.google.auth.oauth2;
    requires com.google.api.apicommon;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires google.cloud.core; // This provides com.google.cloud.Service
    requires com.google.auth; // Required for ApiFuture

    // Allow JavaFX to access your service classes
    opens org.example.testapp.services to javafx.fxml;
    opens org.example.testapp.entities to javafx.base;

    opens org.example.testapp to javafx.fxml;
    exports org.example.testapp;
}