package ru.nsu.pivkin.objs;

import ru.nsu.pivkin.enums.Direction;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Тело змейки: упорядоченный список звеньев от головы к хвосту.
 */
public class Snake {
    private final Deque<Point> body = new ArrayDeque<>();
    private Direction direction;
    private boolean saveTail;

    /**
     * Создаёт змейку с одним звеном в заданной позиции.
     *
     * @param start - стартовая клетка
     * @param direction - начальное направление движения
     */
    public Snake(Point start, Direction direction) {
        this.body.addFirst(start);
        this.direction = direction;
        this.saveTail = false;
    }

    /**
     * Возвращает голову змейки.
     *
     * @return - первое звено
     */
    public Point getHead() {
        return body.peekFirst();
    }

    /**
     * Возвращает тело змейки в виде списка (голова первая).
     *
     * @return - список звеньев
     */
    public List<Point> getBody() {
        return List.copyOf(body);
    }

    /**
     * Возвращает длину змейки.
     *
     * @return - количество звеньев
     */
    public int getLength() {
        return body.size();
    }

    /**
     * Возвращает текущее направление движения.
     *
     * @return - направление
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Задаёт новое направление, если оно не противоположно текущему.
     *
     * @param newDirection - желаемое направление
     */
    public void setDirection(Direction newDirection) {
        if (newDirection != direction.opposite()) {
            direction = newDirection;
        }
    }

    /**
     * Делает шаг в текущем направлении.
     * Если змейка съела еду, хвост не удаляется.
     *
     * @return - новая позиция головы
     */
    public Point move() {
        Point newHead = getHead().step(direction);
        body.addFirst(newHead);

        if (saveTail) {
            saveTail = false;
        } else {
            body.removeLast();
        }

        return newHead;
    }

    /**
     * Помечает, что на следующем шаге змейка вырастет на одно звено.
     */
    public void grow() {
        saveTail = true;
    }

    /**
     * Проверяет, содержит ли тело змейки заданную точку.
     *
     * @param point - проверяемая точка
     * @return - true, если точка является звеном тела
     */
    public boolean contains(Point point) {
        return body.contains(point);
    }
}
