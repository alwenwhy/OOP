package ru.nsu.pivkin.enums;

/**
 * Направление движения змейки.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    /**
     * Возвращает противоположное направление.
     *
     * @return - противоположное направление
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
