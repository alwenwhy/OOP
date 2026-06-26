package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты загрузчика уровней.
 */
class LevelLoaderTest {

    @Test
    void testLoadEmptyRowsReturnsNoWalls() {
        Set<Point> walls = LevelLoader.load(List.of());

        assertTrue(walls.isEmpty());
    }

    @Test
    void testLoadDotsOnlyReturnsNoWalls() {
        Set<Point> walls = LevelLoader.load(List.of("...", "...", "..."));

        assertTrue(walls.isEmpty());
    }

    @Test
    void testLoadHashReturnsWall() {
        Set<Point> walls = LevelLoader.load(List.of("###", "#.#", "###"));

        assertFalse(walls.isEmpty());
        assertTrue(walls.contains(new Point(0, 0)));
        assertTrue(walls.contains(new Point(2, 2)));
    }

    @Test
    void testLoadDoesNotAddDotAsWall() {
        Set<Point> walls = LevelLoader.load(List.of("#.#"));

        assertFalse(walls.contains(new Point(1, 0)));
    }

    @Test
    void testLoadCountsWallsCorrectly() {
        Set<Point> walls = LevelLoader.load(List.of("###", "#.#", "###"));

        assertEquals(8, walls.size());
    }
}
