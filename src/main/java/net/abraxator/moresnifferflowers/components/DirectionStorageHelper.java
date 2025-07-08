package net.abraxator.moresnifferflowers.components;

import net.minecraft.core.Direction;

public class DirectionStorageHelper {

    public static int directionToInt(Direction direction){
        return switch (direction) {
            case DOWN -> 0;
            case UP -> 1;
            case NORTH -> 2;
            case SOUTH -> 3;
            case WEST -> 4;
            case EAST -> 5;
        };
    }

    public static Direction intToDirection(int directionId){
        return switch (directionId) {
            case 0 -> Direction.DOWN;
            case 1 -> Direction.UP;
            case 2 -> Direction.NORTH;
            case 3 -> Direction.SOUTH;
            case 4 -> Direction.WEST;
            case 5 -> Direction.EAST;
            default -> throw new IllegalStateException("Error converting to Direction, Unexpected speed: " + directionId);
        };
    }
}
