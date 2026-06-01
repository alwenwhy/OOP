package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Direction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для Direction.
 */
class DirectionTest {
    @Test
    void testOppositeOfUp() {
        assertEquals(Direction.DOWN, Direction.UP.opposite());
    }

    @Test
    void testOppositeOfDown() {
        assertEquals(Direction.UP, Direction.DOWN.opposite());
    }

    @Test
    void testOppositeOfLeft() {
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite());
    }

    @Test
    void testOppositeOfRight() {
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
    }
}
