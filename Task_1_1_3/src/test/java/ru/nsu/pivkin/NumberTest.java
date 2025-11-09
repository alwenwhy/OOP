package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Number.
 */
public class NumberTest {
    @Test
    void testEvalReturnsValue() {
        Expression num = new Number(5);
        assertEquals(5, ExpressionUtil.evaluate(num, ""));
    }

    @Test
    void testDerivativeIsZero() {
        Expression num = new Number(5);
        Expression d = num.derivative("x");
        assertEquals(0, ExpressionUtil.evaluate(d, "x=10"));
    }
}
