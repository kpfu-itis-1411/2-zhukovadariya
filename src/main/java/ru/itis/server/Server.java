package ru.itis.server;

import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.connection.Connection;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.message.client.PlayerDisconnectedMessage;
import ru.itis.protocol.message.server.CurrentPlayersCountMessage;
import ru.itis.protocol.message.server.PlayerAcceptedMessage;
import ru.itis.protocol.model.Player;
import ru.itis.protocol.model.Role;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static ru.itis.server.ServerConstants.port;
// игровой сервер; отвечает за управление подключениями клиентов, обработку сообщений и управление игровым процессом
public class Server {
    private final HashMap<Integer, Connection> connections = new HashMap<>(); // подключения клиентов к серверу
    private final ServerSocket serverSocket;
    private int userId = 1;
    private final Queue<Role> availableRoles = new ArrayDeque<>();
    {
        availableRoles.add(Role.SNAKE);
        availableRoles.add(Role.FRUIT);
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void start() {
        System.out.println("Server started at port: " + port);
        while (true) {
            try {
                Socket socket = serverSocket.accept(); // принимаем соединения
                System.out.println("Accepted new connection:");
                addConnection(socket);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private Role getAvailableRole() {
        return availableRoles.remove(); // возвращает доступную роль для игрока
    }

    public void addConnection(Socket socket) {
        Player player = Player.builder()
                .id(userId++)
                .role(getAvailableRole())
                .build();
        PlayerConnection connection = new PlayerConnection(socket, this, player); // создаем новое подключение
        connections.put(connection.getId(), connection);
        new Thread(connection).start(); // запускаем новый поток
        sendToConnection(connection.getId(), new PlayerAcceptedMessage(connection.getId(), connection.getPlayer()));
        sendToAll(new CurrentPlayersCountMessage(connections.size()));
        System.out.printf("User with id %s and role %s joined\n", connection.getId(), connection.getRole());
    }

    // отправка сообщения конкретному подключению
    private void sendToConnection(int connectionId, ContentMessage<?> message) {
        Connection con = connections.get(connectionId);
        if (con.isConnected()) {
            con.send(message);
        }
    }

    // отправка сообщения всем подключениям
    public void sendToAll(BasicMessage message) {
        for (Connection connection : connections.values()) {
            if (connection.isConnected()) {
                connection.send(message);
            }
        }
    }

    // отправка сообщения  всем подключениям в connections, кроме подключения с ролью игрока playerConnection
    public void sendToAllOthersByRole(BasicMessage message, PlayerConnection playerConnection) {
        for (Connection connection : connections.values()) {
            if (connection.isConnected() && connection.getRole() != playerConnection.getRole()) {
                connection.send(message);
            }
        }
    }

    // проверка на то, достаточно игроков или нет
    private boolean isEnoughPlayers() {
        return connections.size() == 2;
    }

    // удаление подключения и возврат роли
    private void removeConnection(int id) {
        Connection connection = connections.get(id);
        availableRoles.add(connection.getRole());
        connections.remove(id);
        System.out.println("Removed connection with id " + id);
    }

    // уведомление об удалении
    public void removeConnectionAndNotify(int id) {
        removeConnection(id);
        sendToAll(new CurrentPlayersCountMessage(connections.size()));
    }

    public void removeConnectionAndStopGame(int id) {
        removeConnection(id);
        sendToAll(new PlayerDisconnectedMessage(id));
    }

    public void startGame(BasicMessage message) {
        if (isEnoughPlayers()) {
            sendToAll(message);
        }
    }
}
