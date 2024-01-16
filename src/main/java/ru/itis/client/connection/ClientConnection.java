package ru.itis.client.connection;

import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.model.Player;
import ru.itis.server.ServerConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
// клиентское соединение с сервером
public class ClientConnection {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private Player player;

    public ClientConnection() {
        try {
            socket = new Socket(InetAddress.getByName(ServerConstants.host), ServerConstants.port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            MessageListener messageListenerThread = new MessageListener(socket);
            messageListenerThread.start();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void send(BasicMessage message) {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(message); // записвваем сообщение
            objOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getId() {
        return player.getId();
    }

    public void close()  {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
