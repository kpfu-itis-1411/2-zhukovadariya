package ru.itis.protocol.connection;

import ru.itis.protocol.model.Player;
import ru.itis.protocol.model.Role;

public interface Connection {
    void send(BasicMessage message);
    Player getPlayer();
    Role getRole();
    void close();

    int getId();

    boolean isConnected();
}
