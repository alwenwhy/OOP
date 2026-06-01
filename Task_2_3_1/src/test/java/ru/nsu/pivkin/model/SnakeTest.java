package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Direction;
import ru.nsu.pivkin.model.Point;
import ru.nsu.pivkin.model.Snake;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты класса змейки.
 */
class SnakeTest {
    @Test
    void testInitialLengthIsOne() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);

        assertEquals(1, snake.getLength());
    }

    @Test
    void testInitialHeadPosition() {
        Snake snake = new Snake(new Point(3, 4), Direction.RIGHT);

        assertEquals(new Point(3, 4), snake.getHead());
    }

    @Test
    void testMoveChangesHeadPosition() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.move();

        assertEquals(new Point(6, 5), snake.getHead());
    }

    @Test
    void testMoveLengthStaysTheSame() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.move();

        assertEquals(1, snake.getLength());
    }

    @Test
    void testGrowIncreasesLength() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.grow();
        snake.move();

        assertEquals(2, snake.getLength());
    }

    @Test
    void testSetDirectionChangesDirection() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.setDirection(Direction.UP);

        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void testSetDirectionIgnoresOpposite() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.setDirection(Direction.LEFT);

        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void testContainsHead() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);

        assertTrue(snake.contains(new Point(5, 5)));
    }

    @Test
    void testNotContainsOther() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);

        assertFalse(snake.contains(new Point(0, 0)));
    }

    @Test
    void testBodyListHasCorrectSize() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.grow();
        snake.move();

        assertEquals(2, snake.getBody().size());
    }

    @Test
    void testGetDirectionInitial() {
        Snake snake = new Snake(new Point(0, 0), Direction.DOWN);

        assertEquals(Direction.DOWN, snake.getDirection());
    }
}
