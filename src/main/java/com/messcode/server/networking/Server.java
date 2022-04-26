package com.messcode.server.networking;

import JDBC.ExportData;
import JDBC.ImportData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 */
public class Server {

    private static final int SERVER_PORT = 9090;

    /**
     * This is where the server is started.
     * Serversocket is created,a connection pool which will contain the users who join, all the jdbc import and export classes.
     * It will start a new thread for each client who joins.
     */
    public void start() {
        try {
            InetAddress ip = InetAddress.getByName("192.168.0.105");
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            ConnectionPool pool = new ConnectionPool();
            ImportData dbi = new ImportData();
            ExportData dbe = new ExportData();
            try {System.out.println("MOHAMED");
                dbe.checkDatabaseState();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            while (true) {
                System.out.println("[SERVER] Waiting for client connection");
                Socket socket = serverSocket.accept();
                ServerSocketHandler socketHandler = new ServerSocketHandler(socket,
                        pool, dbi, dbe);
                new Thread(socketHandler).start();
                System.out.println("[SERVER] Connected to client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
