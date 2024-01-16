package ru.itis.protocol.message.client;

import lombok.AllArgsConstructor;
import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;
import ru.itis.protocol.model.FruitCoordinate;

@AllArgsConstructor
public class FruitCoordinatesMessage implements ContentMessage<FruitCoordinate> {
    private final int senderId;
    private final FruitCoordinate coordinate;

    @Override
    public MessageTypes getType() {
        return MessageTypes.FRUIT_COORDINATES;
    }

    @Override
    public int senderId() {
        return senderId;
    }

    @Override
    public FruitCoordinate content() {
        return coordinate;
    }

//    public String toString() {
//        return "Fruit :{x:%s; y:%s}".formatted(coordinate.getX(), coordinate.getY());
//    }
}
