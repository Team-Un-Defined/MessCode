package com.messcode.server;

import com.messcode.server.networking.Server;

/**
 *
 */
public class RunServer {

    /**
     * Server is started here
     *
     * @param args
     */
    public static void main(String[] args) {
        Server es = new Server();
        es.start();
    }
}
