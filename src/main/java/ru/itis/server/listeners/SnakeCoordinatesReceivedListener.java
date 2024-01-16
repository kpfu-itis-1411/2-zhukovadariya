package ru.itis.server.listeners;

import lombok.RequiredArgsConstructor;
import ru.itis.protocol.message.client.SnakeDirectionMessage;
import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;

@RequiredArgsConstructor
public class SnakeCoordinatesReceivedListener implements ClientEventListener{
    private final SnakeDirectionMessage message; // информация о направлении змейки
    private Server server;

    @Override
    public void handleMessage(PlayerConnection connection) {
        // отправляет сообщение всем подключенным игрокам, кроме отправителя
        server.sendToAllOthersByRole(message, connection); // connection-информация о подключенном игроке
    }

    @Override
    public void initServer(Server server) {
        this.server = server;
    }
}
