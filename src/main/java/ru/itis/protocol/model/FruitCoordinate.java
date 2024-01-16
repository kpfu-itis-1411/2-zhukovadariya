package ru.itis.protocol.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class FruitCoordinate implements Serializable {
    private int x;
    private int y;
}
