package com.messcode.client.core;

import com.messcode.client.networking.Client;
import com.messcode.client.networking.SocketClient;

/**
 *This method is responsible for creating and initializaing and returning the same instances of the client object
 */
public class ClientFactory {

    private Client client;

    /**
     * get method
     * @return Client object
     */
    public Client getClient() {
        if (client == null) {
            client = new SocketClient();
        }
        return client;
    }
}
