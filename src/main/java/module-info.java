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
    opens com.messcode.client.views.edit_member to javafx.fxml;
    opens com.messcode.client.views.remove_group to javafx.fxml;
    opens com.messcode.client.views.remove_user to javafx.fxml;
    opens com.messcode.client.views.edit_project_leader to javafx.fxml;
    opens com.messcode.client.views.view_profile to javafx.fxml;
    opens com.messcode.client.views.view_group to javafx.fxml;
    exports com.messcode.client.views.chat;
    exports com.messcode.client.core;
    exports com.messcode.client;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.postgresql.jdbc;
}
