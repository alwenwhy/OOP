package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.enums.Direction;
import ru.nsu.pivkin.objs.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты класса точки.
 */
class PointTest {
    @Test
    void testGetXAndGetY() {
        Point p = new Point(3, 7);

        assertEquals(3, p.getX());
        assertEquals(7, p.getY());
    }

    @Test
    void testStepUp() {
        assertEquals(new Point(5, 4), new Point(5, 5).step(Direction.UP));
    }

    @Test
    void testStepDown() {
        assertEquals(new Point(5, 6), new Point(5, 5).step(Direction.DOWN));
    }

    @Test
    void testStepLeft() {
        assertEquals(new Point(4, 5), new Point(5, 5).step(Direction.LEFT));
    }

    @Test
    void testStepRight() {
        assertEquals(new Point(6, 5), new Point(5, 5).step(Direction.RIGHT));
    }

    @Test
    void testEqualsSameCoords() {
        assertEquals(new Point(1, 2), new Point(1, 2));
    }

    @Test
    void testNotEqualsDifferentCoords() {
        assertNotEquals(new Point(1, 2), new Point(2, 1));
    }

    @Test
    void testHashCodeConsistentWithEquals() {
        Point a = new Point(4, 4);
        Point b = new Point(4, 4);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToStringContainsCoords() {
        String str = new Point(3, 7).toString();

        assertTrue(str.contains("3"));
        assertTrue(str.contains("7"));
    }
}
