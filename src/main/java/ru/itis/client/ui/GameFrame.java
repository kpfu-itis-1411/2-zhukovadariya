package ru.itis.client.ui;

import ru.itis.client.connection.ConnectionHolder;
import ru.itis.protocol.message.client.PlayerDisconnectedMessage;
import ru.itis.protocol.model.FruitCoordinate;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;

    public GameFrame() {
    }

    public void init() {
        var conn = ConnectionHolder.getConnection();
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("SnakeGame " + conn.getPlayer().getRole());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                conn.send(new PlayerDisconnectedMessage(conn.getId()));
                conn.close();
                dispose();
                System.exit(0);
            }
        });
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void setFruitCoordinates(FruitCoordinate fruitCoordinates) {
        gamePanel.newFruits(fruitCoordinates);
    }

    public void setSnakeDirection(String direction) {
        gamePanel.setSnakeDirection(direction);
    }

    public void disconnectionGameOver() {
        gamePanel.playerDisconnectedGameOver();
    }
}
