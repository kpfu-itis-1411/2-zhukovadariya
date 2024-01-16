package ru.itis.client.util;

import ru.itis.client.connection.ConnectionHolder;

public class ServerConnector  {

    public static void start() {
        new ServerConnector();
    }
    private ServerConnector() {
        connectToServer();
    }

    private void connectToServer() {
        ConnectionHolder.initializeConnection();
    }
}

