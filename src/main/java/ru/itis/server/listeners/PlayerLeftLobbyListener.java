package ru.itis.server.listeners;

import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;
// обработка сообщения об отключении игрока от сервера
// удаление соответствующего подключение из очереди подключений сервера и уведомляет об этом всех остальных игроков в лобби
public class PlayerLeftLobbyListener implements ClientEventListener {
    private Server server;

    @Override
    public void handleMessage(PlayerConnection connection) {
        server.removeConnectionAndNotify(connection.getId());
    }

    @Override
    public void initServer(Server server) {
        this.server = server;
    }
}