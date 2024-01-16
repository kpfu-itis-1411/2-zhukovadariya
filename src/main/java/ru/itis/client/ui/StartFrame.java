package ru.itis.client.ui;

import ru.itis.client.connection.ConnectionHolder;
import ru.itis.protocol.message.client.StartGameMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartFrame extends JFrame {
    private JLabel currentConnectedPlayers;

    public StartFrame() {
    }


    public void redrawCount(int count) {
        this.currentConnectedPlayers.setText("Игроков сейчас подключено: " + count);
    }


    public void initFrame(int count) {
        JLabel helloLabel = new JLabel("Добро пожаловать в игру Змейка! Игру можно начать, когда наберется ровно 2 игрока");
        currentConnectedPlayers = new JLabel("Игроков сейчас подключено: " + count);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helloLabel.setVerticalAlignment(SwingConstants.TOP);
        currentConnectedPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        currentConnectedPlayers.setVerticalAlignment(SwingConstants.TOP);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BorderLayout());
        panel.add(helloLabel, BorderLayout.NORTH);
        panel.add(currentConnectedPlayers, BorderLayout.PAGE_END);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton startButton = new JButton("Начать игру");

        startButton.addActionListener(e -> ConnectionHolder.getConnection().send(new StartGameMessage(ConnectionHolder.getConnection().getId())));

        buttonPanel.add(startButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConnectionHolder.closeConnection();
                dispose();
                System.exit(0);
            }
        });
        this.add(panel);
        this.setTitle("snakeOld.SnakeGame");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(600, 150);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}