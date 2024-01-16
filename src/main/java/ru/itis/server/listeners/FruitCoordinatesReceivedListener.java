package ru.itis.server.listeners;

import lombok.RequiredArgsConstructor;
import ru.itis.protocol.message.client.FruitCoordinatesMessage;
import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;
// обработка сообщения о координатах фрукта
// отправка сообщения о координатах фрукта всем игрокам, кроме того, который отправил это сообщение
@RequiredArgsConstructor
public class FruitCoordinatesReceivedListener implements ClientEventListener {
    private final FruitCoordinatesMessage message;
    private Server server;

    @Override
    public void handleMessage(PlayerConnection connection) {
        server.sendToAllOthersByRole(message, connection);
    }

    @Override
    public void initServer(Server server) {
        this.server = server;
    }
}
