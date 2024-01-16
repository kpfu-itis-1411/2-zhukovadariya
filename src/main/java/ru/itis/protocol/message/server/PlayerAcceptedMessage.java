package ru.itis.protocol.message.server;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;
import ru.itis.protocol.model.Player;


@AllArgsConstructor
public class PlayerAcceptedMessage implements ContentMessage<Player> {
    private final int senderId;

    private Player content;
    @Override
    public MessageTypes getType() {
        return MessageTypes.PLAYER_ACCEPTED;
    }

    @Override
    public int senderId() {
        return senderId;
    }

    @Override
    public Player content() {
        return content;
    }

    public String toString() {
        return "Player %s with role %s accepted to lobby!".formatted(content.getId(), content.getRole());
    }
}
