package ru.itis.protocol.message.client;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.BasicMessage;
import ru.itis.protocol.constants.MessageTypes;

@AllArgsConstructor
public class StartGameMessage implements BasicMessage {
    private final int playerId;

    @Override
    public MessageTypes getType() {
        return MessageTypes.GAME_START;
    }

    @Override
    public int senderId() {
        return playerId;
    }
    public String toString() {
        return "Player %s started the game!".formatted(playerId);
    }
}
