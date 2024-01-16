package ru.itis.protocol.message.client;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;

@AllArgsConstructor
public class SnakeDirectionMessage implements ContentMessage<String> {
    private final int senderId;
    private final String direction;

    @Override
    public MessageTypes getType() {
        return MessageTypes.SNAKE_COORDINATES;
    }

    @Override
    public int senderId() {
        return senderId;
    }

    @Override
    public String content() {
        return direction;
    }
}
