package ru.nsu.pivkin.objs;

import ru.nsu.pivkin.enums.Direction;
import java.util.Objects;

/**
 * Координата клетки игрового поля.
 */
public class Point {
    private final int x;
    private final int y;

    /**
     * Создаёт точку с заданными координатами.
     *
     * @param x - столбец
     * @param y - строка
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает столбец.
     *
     * @return - x
     */
    public int getX() {
        return x;
    }

    /**
     * Возвращает строку.
     *
     * @return - y
     */
    public int getY() {
        return y;
    }

    /**
     * Возвращает соседнюю точку в заданном направлении.
     *
     * @param direction - направление шага
     * @return - новая точка
     */
    public Point step(Direction direction) {
        return switch (direction) {
            case UP -> new Point(x, y - 1);
            case DOWN -> new Point(x, y + 1);
            case LEFT -> new Point(x - 1, y);
            case RIGHT -> new Point(x + 1, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Point point)) {
            return false;
        }

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
