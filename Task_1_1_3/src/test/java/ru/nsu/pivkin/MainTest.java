package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тест основной программы.
 */
public class MainTest {
    @Test
    void checkMain() {
        Main.main(new String[] {});
        assertTrue(true);
    }
}
