package ru.itis.server.listeners;

import ru.itis.protocol.message.client.StartGameMessage;
import ru.itis.server.PlayerConnection;
import ru.itis.server.Server;
// реализация интерфейса ClientEventListener, который слушает сообщения от клиента,
// содержащие информацию о начале игры, и передает эту информацию объекту сервера для дальнейшей обработки
public class StartGameListener implements ClientEventListener{
    private Server server;

    private final StartGameMessage message;

    public StartGameListener(StartGameMessage message) {
        this.message = message;
    }

    public void initServer(Server server){
        this.server = server;
    }

    @Override
    public void handleMessage(PlayerConnection connection) {
        server.startGame(message);
    }
}
