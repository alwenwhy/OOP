package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Point;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты загрузчика уровней.
 */
class LevelLoaderTest {
    @Test
    void testLoadDefaultReturnsEmptyWalls() {
        Set<Point> walls = LevelLoader.load("default.txt");

        assertTrue(walls.isEmpty());
    }

    @Test
    void testLoadLevel1HasWalls() {
        Set<Point> walls = LevelLoader.load("level1.txt");

        assertFalse(walls.isEmpty());
    }

    @Test
    void testLoadLevel1HasTopLeftCorner() {
        Set<Point> walls = LevelLoader.load("level1.txt");

        assertTrue(walls.contains(new Point(0, 0)));
    }

    @Test
    void testLoadMissingFileReturnsEmpty() {
        Set<Point> walls = LevelLoader.load("nonexistent.txt");
        
        assertTrue(walls.isEmpty());
    }
}
