package ru.itis.server;

import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.connection.Connection;
import ru.itis.protocol.model.Player;
import ru.itis.protocol.model.Role;
import ru.itis.server.listeners.ClientEventListener;
import ru.itis.server.listeners.ServerEventListener;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static ru.itis.server.ServerConstants.DELAY;
//  соединение между игроком и сервером
public class PlayerConnection implements Connection, Runnable {
    private final Socket socket; // сетевое соединение между клиентом и сервером
    private final InputStream in; // чтение данных из сокета
    private final OutputStream out; // запись данных в сокет
    private final Server server;
    private final Player player;

    public PlayerConnection(Socket socket, Server server, Player player) {
        this.socket = socket;
        this.server = server;
        this.player = player;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void send(BasicMessage message) {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(message);
            objOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException ignored) {
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Role getRole() {
        return player.getRole();
    }

    @Override
    public void run() { // запускается при запуске потока
        try {
            while (socket.isConnected()) {
                int b = in.available(); // наличие данных в сокете
                if (b != 0) {
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    BasicMessage message = (BasicMessage) objIn.readObject(); // считываем и десериализуем
                    ClientEventListener listener = ServerEventListener.getListener(message.getType(), message); // обрабатывает сообщение и вызывает соответствующий метод сервера
                    listener.initServer(server);
                    listener.handleMessage(this);
                }
            }
        } catch (SocketException ignored){

        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getId() {
        return player.getId();
    }

    @Override
    public boolean isConnected() { // подключен клиент к серверу?
        return !socket.isClosed();
    }
}