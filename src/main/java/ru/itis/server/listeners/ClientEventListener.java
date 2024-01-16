package ru.itis.server.listeners;

import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;

public interface ClientEventListener {
    void handleMessage(PlayerConnection connection);
    void initServer(Server server);
}
