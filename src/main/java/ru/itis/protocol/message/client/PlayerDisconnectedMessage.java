package ru.itis.protocol.message.client;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.constants.MessageTypes;

@AllArgsConstructor
public class PlayerDisconnectedMessage implements BasicMessage {
    private final int playerId;

    @Override
    public MessageTypes getType() {
        return MessageTypes.PLAYER_DISCONNECT;
    }

    @Override
    public int senderId() {
        return playerId;
    }

    public String toString() {
        return "Player with id " + playerId + " disconnected from the game";
    }
}