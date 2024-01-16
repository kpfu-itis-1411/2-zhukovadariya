package ru.itis.server.listeners;

import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;
// обработка сообщения об отключении игрока от сервера
// удаление соответствующего подключение из очереди подключений сервера и остановка игры, если она была запущена на этом соединении
public class PlayerDisconnectedListener implements ClientEventListener {
    private Server server;

    @Override
    public void handleMessage(PlayerConnection connection) {
        server.removeConnectionAndStopGame(connection.getId());
    }

    @Override
    public void initServer(Server server) {
        this.server = server;
    }
}
