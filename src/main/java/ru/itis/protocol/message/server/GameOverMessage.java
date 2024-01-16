package ru.itis.protocol.message.server;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;


@AllArgsConstructor
public class GameOverMessage implements ContentMessage<Integer> {
    private final int points;

    @Override
    public MessageTypes getType() {
        return MessageTypes.GAME_OVER;
    }

    @Override
    public int senderId() {
        return 0;
    }

    @Override
    public Integer content() {
        return points;
    }

    public String toString() {
        return "Game has ended";
    }
}
