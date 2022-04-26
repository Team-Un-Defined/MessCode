package com.messcode.client;

import com.messcode.client.core.ClientFactory;
import com.messcode.client.core.ModelFactory;
import com.messcode.client.core.ViewHandler;
import com.messcode.client.core.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 *
 */
public class RunApp extends Application {

    /**
     * All the factories are initialized here
     * @param stage Stage object
     */
    @Override
    public void start(Stage stage) {
        ClientFactory cf = new ClientFactory();
        ModelFactory mf = new ModelFactory(cf);
        ViewModelFactory vmf = new ViewModelFactory(mf);
        ViewHandler vh = new ViewHandler(vmf);
        vh.start();
    }
}
