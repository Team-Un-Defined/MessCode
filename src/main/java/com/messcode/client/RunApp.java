package com.messcode.client;

import com.messcode.client.core.ClientFactory;
import com.messcode.client.core.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import com.messcode.client.core.ModelFactory;
import com.messcode.client.core.ViewHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;


public class RunApp extends Application {

    @Override
    public void start(Stage stage) {


        ClientFactory cf = new ClientFactory();
        ModelFactory mf = new ModelFactory(cf);
        ViewModelFactory vmf = new ViewModelFactory(mf);
        ViewHandler vh = new ViewHandler(vmf);
        vh.start();

    }
}
