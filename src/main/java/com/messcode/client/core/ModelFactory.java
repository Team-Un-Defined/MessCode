package com.messcode.client.core;

import com.messcode.client.model.MainModel;
import com.messcode.client.model.MainModelManager;

public class ModelFactory {

    private ClientFactory cf;
    private MainModel mainModel;

    public ModelFactory(ClientFactory cf) {
        this.cf = cf;
    }

    public MainModel getMainModel() {
        if (mainModel == null)
            mainModel = new MainModelManager(cf.getClient());
        return mainModel;
    }
}
