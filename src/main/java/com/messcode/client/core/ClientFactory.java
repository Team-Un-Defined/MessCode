package com.messcode.client.core;

import com.messcode.client.networking.Client;
import com.messcode.client.networking.SocketClient;

/**
 *
 */
public class ClientFactory {

    private Client client;

    /**
     * @return
     */
    public Client getClient() {
        if (client == null) {
            client = new SocketClient();
        }
        return client;
    }
}
