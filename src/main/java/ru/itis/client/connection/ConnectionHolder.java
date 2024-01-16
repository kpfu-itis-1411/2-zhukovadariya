package ru.itis.client.connection;

import ru.itis.protocol.message.client.PlayerLeftLobbyMessage;
// хранилище клиентского соединения с сервером
public class ConnectionHolder {
    private static ClientConnection connection;

    public static ClientConnection getConnection() { // при утере соединения
        if (connection == null) {
            throw new RuntimeException("Connection lost");
        }
        return connection;
    }

    public static void initializeConnection() { // если еще не было создано
        if (connection == null) {
            connection = new ClientConnection();
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            if (connection.getPlayer() != null) {
                int playerId = connection.getId();
                System.out.println("Disconnecting from server");
                connection.send(new PlayerLeftLobbyMessage(playerId)); // отправка сообщения о том, что игрок покидает лобби
            }
            connection.close();
        }
    }
}
