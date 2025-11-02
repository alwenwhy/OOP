package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    @Test
    void checkMain() {
        Main.main(new String[] {});
        assertTrue(true);
    }
}
