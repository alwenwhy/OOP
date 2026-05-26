package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты конфига.
 */
class GameConfigTest {
    @Test
    void testLoadReturnsNonNull() {
        assertNotNull(GameConfig.load());
    }

    @Test
    void testDefaultColsPositive() {
        assertTrue(GameConfig.load().getCols() > 0);
    }

    @Test
    void testDefaultRowsPositive() {
        assertTrue(GameConfig.load().getRows() > 0);
    }

    @Test
    void testDefaultFoodCountNonNegative() {
        assertTrue(GameConfig.load().getFoodCount() >= 0);
    }

    @Test
    void testDefaultWinLengthPositive() {
        assertTrue(GameConfig.load().getWinLength() > 0);
    }

    @Test
    void testDefaultTickMsPositive() {
        assertTrue(GameConfig.load().getTickMs() > 0);
    }

    @Test
    void testDefaultLevelNotEmpty() {
        assertFalse(GameConfig.load().getLevel().isEmpty());
    }
}
