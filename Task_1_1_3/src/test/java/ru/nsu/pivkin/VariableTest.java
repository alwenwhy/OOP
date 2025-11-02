package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariableTest {
    @Test
    void testEvalUsesVariableValue() {
        Expression v = new Variable("x");
        assertEquals(10, v.eval("x=10"));
    }

    @Test
    void testEvalThrowsIfMissingVariable() {
        Expression v = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> v.eval(""));
    }

    @Test
    void testDerivativeIsOneOrZero() {
        Expression vx = new Variable("x");
        Expression vy = new Variable("y");
        assertEquals(1, vx.derivative("x").eval(""));
        assertEquals(0, vy.derivative("x").eval(""));
    }
}