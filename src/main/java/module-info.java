module com.example.messcode {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires java.sql;
   

    opens com.messcode.client.views.login to javafx.fxml;
    opens com.messcode.client.views.chat to javafx.fxml;
    opens com.messcode.client.views.new_employee to javafx.fxml;
    opens com.messcode.client.views.new_group to javafx.fxml;
    opens com.messcode.client.views.change_password to javafx.fxml;
    exports com.messcode.client.views.chat;
    exports com.messcode.client.core;
    exports com.messcode.client;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

   
}
