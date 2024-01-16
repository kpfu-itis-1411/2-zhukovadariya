package ru.itis.client.ui;

import ru.itis.client.connection.ClientConnection;
import ru.itis.client.connection.ConnectionHolder;
import ru.itis.protocol.message.client.FruitCoordinatesMessage;
import ru.itis.protocol.message.client.SnakeDirectionMessage;
import ru.itis.protocol.model.FruitCoordinate;
import ru.itis.protocol.model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import static ru.itis.server.ServerConstants.DELAY;


public class GamePanel extends JPanel implements ActionListener {

    public final int SCREEN_WIDTH = 600;
    public final int SCREEN_HEIGHT = 600;
    public final int UNIT_SIZE = 25;
    public final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // сколько сможем разместить на экране
    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];
    private int bodyParts = 6;
    private int fruitsEaten;
    private int fruitsX;
    private int fruitsY;
    private boolean playerDisconnected = false;
    private String snakeDirection = "RIGHT";
    private String fruitDirection = "STAY";
    private boolean running = false;
    private final Timer timer = new Timer(DELAY, this);
    private final Random random = new Random();


    public GamePanel() {
        Dimension dimension = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setPreferredSize(dimension);

        this.setBackground(Color.BLACK);

        this.setFocusable(true);
        MyKeyAdapter myKeyAdapter = new MyKeyAdapter();
        this.addKeyListener(myKeyAdapter);
        startGame();
    }

    private void startGame() {
        newFruits();
        fruitDirection = "STAY";
        running = true;
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        if (running) {
            super.paintComponent(graphics);
            drawSnakeBodyParts(graphics);
            drawFruit(graphics);
            drawScore(graphics);
        } else if (!playerDisconnected) {
            gameOver();
        }
    }

    public void drawFruit(Graphics graphics) {
        graphics.setColor(new Color(176, 35, 49));
        graphics.fillOval(fruitsX, fruitsY, UNIT_SIZE, UNIT_SIZE);
    }

    public void drawScore(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("SCORE: " + fruitsEaten, (SCREEN_WIDTH - fontMetrics.stringWidth("SCORE: " + fruitsEaten)) / 2, graphics.getFont().getSize());
    }

    public void drawSnakeBodyParts(Graphics graphics) {
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                graphics.setColor(new Color(255, 255, 0));
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                graphics.setColor(new Color(215, 181, 14));
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void newFruits() {
        if (ConnectionHolder.getConnection().getPlayer().getRole()==Role.FRUIT) {
            fruitsX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            fruitsY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
            sendFruitCoordinates(ConnectionHolder.getConnection());
        }
    }

    private void sendFruitCoordinates(ClientConnection conn) {
        if (conn.getPlayer().getRole() == Role.FRUIT) {
            conn.send(new FruitCoordinatesMessage(conn.getId(), new FruitCoordinate(fruitsX, fruitsY)));
            System.out.println("Player " + ConnectionHolder.getConnection().getId() + " sent fruit : {x: " + fruitsX + ", y:" + fruitsY + "}");
        }
    }

    public void newFruits(FruitCoordinate fruitCoordinate) {
        fruitsX = fruitCoordinate.getX();
        fruitsY = fruitCoordinate.getY();
        sendFruitCoordinates(ConnectionHolder.getConnection());
    }

    public void setSnakeDirection(String direction) {
        snakeDirection = direction;
    }

    public void moveFruit() {
        switch (fruitDirection) {
            case "UP" -> fruitsY = fruitsY - UNIT_SIZE;
            case "DOWN" -> fruitsY = fruitsY + UNIT_SIZE;
            case "LEFT" -> fruitsX = fruitsX - UNIT_SIZE;
            case "RIGHT" -> fruitsX = fruitsX + UNIT_SIZE;
        }
        fruitDirection = "STAY";
        sendFruitCoordinates(ConnectionHolder.getConnection());
    }

    private void moveSnake() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (snakeDirection) {
            case "UP" -> y[0] = y[0] - UNIT_SIZE;
            case "DOWN" -> y[0] = y[0] + UNIT_SIZE;
            case "LEFT" -> x[0] = x[0] - UNIT_SIZE;
            case "RIGHT" -> x[0] = x[0] + UNIT_SIZE;
        }
        var conn = ConnectionHolder.getConnection();
        sendSnakeCoordinates(conn);
    }

    private void sendSnakeCoordinates(ClientConnection conn) {
        if (conn.getPlayer().getRole() == Role.SNAKE) {
            conn.send(new SnakeDirectionMessage(conn.getId(), snakeDirection));
            System.out.println("Player " + ConnectionHolder.getConnection().getId() + " sent Snake : {x: " + x[0] + ", y:" + y[0] + "}");
        }
    }

    private void checkFruits() {
        if ((x[0] == fruitsX) && (y[0] == fruitsY)) {
            bodyParts++;
            fruitsEaten++;
            newFruits();
        }
    }

    private void checkSelfCrossing() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i] && y[0] == y[i])) {
                running = false;
                break;
            }
        }
    }

    private void checkFruitFail() {
        if (fruitsX < 0 || fruitsX > SCREEN_WIDTH) {
            running = false;
        }
        if (fruitsY < 0 || fruitsY > SCREEN_HEIGHT) {
            running = false;
        }
    }

    private void checkSnakeFail() {
        //проверка на столкновение с телом
        checkSelfCrossing();

        //проверка на столкновение с границами:
        //левая граница
        if (x[0] < 0) {
            running = false;
        }
        //правая граница
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //верхняя граница
        if (y[0] < 0) {
            running = false;
        }
        //нижняя граница
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void playerDisconnectedGameOver() {
        playerDisconnected = true;
        int option = JOptionPane.showOptionDialog(null, "Другой игрок покинул игру. Игра окончена. Ваш результат: " + fruitsEaten,
                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Выйти"}, null);
        if (option == 0 || option == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
    }

    public void gameOver() {
        // Открыть диалоговое окно с кнопками "Начать игру заново" и "Выйти"
        int option = JOptionPane.showOptionDialog(null, "Вы проиграли! Ваш результат: " + fruitsEaten,
                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Начать игру заново", "Выйти"}, null);

        // Обработка выбора пользователя
        if (option == 0) { // Начать игру заново
            fruitsEaten = 0;
            bodyParts = 6;
            snakeDirection = "RIGHT";
            for (int i = 0; i < bodyParts; i++) {
                x[i] = 0;
                y[i] = 0;
            }
            startGame();
        } else { // Выйти
            System.exit(0);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            moveFruit();
            checkFruits();
            checkFruitFail();
            checkSnakeFail();
        }
        repaint();// вызывает метод paint component
    }

    public class MyKeyAdapter extends KeyAdapter {
        boolean isPlayerSnake = ConnectionHolder.getConnection().getPlayer().getRole() == Role.SNAKE;
        boolean isPlayerFruit = ConnectionHolder.getConnection().getPlayer().getRole() == Role.FRUIT;
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (!snakeDirection.equals("RIGHT") && isPlayerSnake) {
                        snakeDirection = "LEFT";
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (!snakeDirection.equals("LEFT") && isPlayerSnake) {
                        snakeDirection = "RIGHT";
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (!snakeDirection.equals("DOWN") && isPlayerSnake) {
                        snakeDirection = "UP";
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (!snakeDirection.equals("UP") && isPlayerSnake) {
                        snakeDirection = "DOWN";
                    }
                }
                case KeyEvent.VK_W -> {
                    if (isPlayerFruit) fruitDirection = "UP";
                }
                case KeyEvent.VK_A -> {
                    if (isPlayerFruit) fruitDirection = "LEFT";
                }
                case KeyEvent.VK_S -> {
                    if(isPlayerFruit) fruitDirection = "DOWN";
                }
                case KeyEvent.VK_D -> {
                    if (isPlayerFruit) fruitDirection = "RIGHT";
                }
            }
        }
    }
}
