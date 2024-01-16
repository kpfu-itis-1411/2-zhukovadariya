package ru.itis.protocol.message.server;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;

@AllArgsConstructor
public class CurrentPlayersCountMessage implements ContentMessage<Integer> {
    private final int count;

    @Override
    public MessageTypes getType() {
        return MessageTypes.PLAYERS_COUNT;
    }

    @Override
    public int senderId() {
        return -1;
    }

    @Override
    public Integer content() {
        return count;
    }

    public String toString() {
        return "Current connected to lobby players :" + count;
    }
}
