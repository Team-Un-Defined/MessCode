package com.messcode.client.core;

import com.messcode.client.model.MainModel;
import com.messcode.client.model.MainModelManager;

/**
 *This class is responsible for initializing and returning the same instance for the mainModelManager class
 */
public class ModelFactory {

    private ClientFactory cf;
    private MainModel mainModel;

    /**
     * Consturctor setting the clientfactory
     * @param cf ClientFactory
     */
    public ModelFactory(ClientFactory cf) {
        this.cf = cf;
    }

    /**
     * get method
     * @return mainModel object
     */
    public MainModel getMainModel() {
        if (mainModel == null){
            this.mainModel = new MainModelManager(cf.getClient());
        }
        return mainModel;
    }
}
