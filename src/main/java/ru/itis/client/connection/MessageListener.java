package ru.itis.client.connection;

import ru.itis.client.exception.ConnectionNotInitializedException;
import ru.itis.client.util.UiEventHandler;
import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.message.client.FruitCoordinatesMessage;
import ru.itis.protocol.message.client.SnakeDirectionMessage;
import ru.itis.protocol.message.server.CurrentPlayersCountMessage;
import ru.itis.protocol.message.server.PlayerAcceptedMessage;
import ru.itis.protocol.message.server.SystemMessage;
import ru.itis.protocol.model.FruitCoordinate;
import ru.itis.protocol.model.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
// слушает сообщения от сервера
public class MessageListener extends Thread {
    private final InputStream in;
    private final UiEventHandler handler; // объект, который обрабатывает сообщения от сервера и обновляет пользовательский интерфейс
    private final Socket socket;

    public MessageListener(Socket socket) throws IOException {
        this.socket = socket;
        this.handler = new UiEventHandler();
        this.in = socket.getInputStream();
    }

    @Override
    public void run() { // выполняется пока соединение открыто
        try {
            while (socket.isConnected()) {
                int b = in.available();
                if (b != 0) {
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    BasicMessage message = (BasicMessage) objIn.readObject();
                    System.out.println(message.toString());
                    handleMessage(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ConnectionNotInitializedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMessage(BasicMessage message) throws ConnectionNotInitializedException {
        switch (message.getType()) {
            case GAME_OVER -> {

            }
            //если сообщение имеет тип SYSTEM_MESSAGE, то получаем содержимое сообщения и передаем его объекту UiEventHandler,
            // который отображает его на пользовательском интерфейсе
            case SYSTEM_MESSAGE -> {
                String serverMessage = ((SystemMessage) message).content();
                handler.showSystemMessage(serverMessage);
            }
            // получаем объект Player из сообщения и сохраняет его в объекте ConnectionHolder.
            // Затем вызываем метод showStartFrame() объекта UiEventHandler, который отображает начальный экран игры
            case PLAYER_ACCEPTED -> {
                Player player = ((PlayerAcceptedMessage) message).content();
                ConnectionHolder.getConnection().setPlayer(player);
                handler.showStartFrame(1);
            }
            case PLAYERS_COUNT -> {
                int count = ((CurrentPlayersCountMessage) message).content();
                handler.redrawCurrentPlayers(count);
            }
            case PLAYER_DISCONNECT -> handler.showDisconnectionGameOver();

            case FRUIT_COORDINATES -> {
                FruitCoordinate coordinate = ((FruitCoordinatesMessage) message).content();
                handler.redrawFruit(coordinate);
            }
            case SNAKE_COORDINATES -> {
                String direction = ((SnakeDirectionMessage) message).content();
                handler.redrawSnake(direction);
            }
            case GAME_START -> handler.showGameFrame();

            default -> handler.showSystemMessage("Unknown message from server");
        }
    }
}
