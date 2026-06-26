package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты конфига.
 */
class GameConfigTest {

    @Test
    void testDefaultColsPositive() {
        assertTrue(new GameConfig().getCols() > 0);
    }

    @Test
    void testDefaultRowsPositive() {
        assertTrue(new GameConfig().getRows() > 0);
    }

    @Test
    void testDefaultFoodCountNonNegative() {
        assertTrue(new GameConfig().getFoodCount() >= 0);
    }

    @Test
    void testDefaultWinLengthPositive() {
        assertTrue(new GameConfig().getWinLength() > 0);
    }

    @Test
    void testDefaultTickMsPositive() {
        assertTrue(new GameConfig().getTickMs() > 0);
    }

    @Test
    void testDefaultActiveLevelNotEmpty() {
        assertFalse(new GameConfig().getActiveLevel().isEmpty());
    }

    @Test
    void testDefaultActiveLevelRowsEmptyBeforeLoading() {
        assertTrue(new GameConfig().getActiveLevelRows().isEmpty());
    }
}
