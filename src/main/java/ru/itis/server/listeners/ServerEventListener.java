package ru.itis.server.listeners;

import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.constants.MessageTypes;
import ru.itis.protocol.message.client.FruitCoordinatesMessage;
import ru.itis.protocol.message.client.SnakeDirectionMessage;
import ru.itis.protocol.message.client.StartGameMessage;

// создание объектов слушателей в зависимости от типа сообщения; использование их для обработки сообщений от клиентов на сервере
public class ServerEventListener {
    public static ClientEventListener getListener(MessageTypes type, BasicMessage message){
        switch (type) {
            case GAME_START -> {
                return new StartGameListener((StartGameMessage) message);
            }
            case PLAYER_DISCONNECT -> {
                return new PlayerDisconnectedListener();
            }
            case PLAYER_LEFT_LOBBY -> {
                return new PlayerLeftLobbyListener();
            }
            case FRUIT_COORDINATES -> {
                return new FruitCoordinatesReceivedListener((FruitCoordinatesMessage) message);
            }
            case SNAKE_COORDINATES -> {
                return new SnakeCoordinatesReceivedListener((SnakeDirectionMessage) message);
            }
        }
        return null;
    }
}
