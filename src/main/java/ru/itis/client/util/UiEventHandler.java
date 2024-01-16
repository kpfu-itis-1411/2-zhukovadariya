package ru.itis.client.util;

import ru.itis.client.connection.ConnectionHolder;
import ru.itis.client.ui.ErrorDialog;
import ru.itis.client.ui.GameFrame;
import ru.itis.client.ui.StartFrame;
import ru.itis.protocol.model.FruitCoordinate;

public class UiEventHandler {
    private final StartFrame startFrame = new StartFrame();
    private final GameFrame gameFrame = new GameFrame();

    public void showSystemMessage(String content) {
        ErrorDialog.showError(content);
    }

    public void showDisconnectionGameOver() {
        gameFrame.disconnectionGameOver();
    }

    public void showStartFrame(int count) {
        startFrame.initFrame(count);
    }

    public void redrawCurrentPlayers(int count) {
        startFrame.redrawCount(count);
    }

    public void redrawFruit(FruitCoordinate coordinate) {
        gameFrame.setFruitCoordinates(coordinate);
        System.out.printf("Player " + ConnectionHolder.getConnection().getId() + " received fruit : {x: %s, y: %s}\n", coordinate.getX(), coordinate.getY());
    }

    public void redrawSnake(String direction) {
        gameFrame.setSnakeDirection(direction);
        System.out.printf("Player " + ConnectionHolder.getConnection().getId() + " received snake direction : %s\n", direction);
    }

    public void showGameFrame() {
        startFrame.dispose();
        gameFrame.init();
    }
}
