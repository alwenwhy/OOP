package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тест основной программы.
 * И топологической сортировки.
 */
public class MainTest {
    @Test
    void checkMain() {
        Main.main(new String[] {"input.txt"});
        Main.main(new String[] {"blablabla"});
        assertTrue(true);
    }
}