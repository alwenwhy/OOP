package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Variable.
 */
public class VariableTest {
    @Test
    void testEvalUsesVariableValue() {
        Expression v = new Variable("x");
        assertEquals(10, ExpressionUtil.evaluate(v, "x=10"));
    }

    @Test
    void testEvalThrowsIfMissingVariable() {
        Expression v = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> ExpressionUtil.evaluate(v, ""));
    }

    @Test
    void testDerivativeIsOneOrZero() {
        Expression vx = new Variable("x");
        Expression vy = new Variable("y");
        assertEquals(1, ExpressionUtil.evaluate(vx.derivative("x"), ""));
        assertEquals(0, ExpressionUtil.evaluate(vy.derivative("x"), ""));
    }
}