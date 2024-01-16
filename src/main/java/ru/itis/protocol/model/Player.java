package ru.itis.protocol.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@Data
public class Player implements Serializable {
    private int id;
    private Role role;
    private int points;
    private int roomId;
}
