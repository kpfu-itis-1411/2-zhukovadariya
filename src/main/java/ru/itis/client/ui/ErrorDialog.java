package ru.itis.client.ui;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog extends JFrame {

    public static void showError(String errorMessage) {
        new ErrorDialog(errorMessage);
    }

    private ErrorDialog(String errorMessage) {
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        JLabel helloLabel = new JLabel(errorMessage);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helloLabel.setVerticalAlignment(SwingConstants.TOP);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(exitButton, BorderLayout.SOUTH);
        panel.add(helloLabel, BorderLayout.NORTH);

        this.add(panel);
        this.setTitle("Ошибка");
        this.setSize(300, 150);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
