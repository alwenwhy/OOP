package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Add.
 */
public class AddTest {
    @Test
    void testAdditionNums() {
        Expression e = new Add(new Number(3), new Number(42));
        assertEquals(45, ExpressionUtil.evaluate(e, ""));
    }

    @Test
    void testAdditionEval() {
        Expression e = new Add(new Number(3), new Variable("x"));
        assertEquals(8, ExpressionUtil.evaluate(e, "x=5"));
    }

    @Test
    void testDerivativeOfAddition() {
        Expression e = new Add(new Variable("x"), new Variable("x"));
        Expression d = e.derivative("x");
        assertEquals(2, ExpressionUtil.evaluate(d, "x=0")); // (1+1)
    }
}
