package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Div.
 */
public class DivTest {
    @Test
    void testDivisionEval() {
        Expression e = new Div(new Variable("x"), new Number(2));
        assertEquals(5, e.eval("x=10"));
    }

    @Test
    void testDerivativeOfDivision() {
        Expression e = new Div(new Variable("x"), new Variable("x"));
        Expression d = e.derivative("x");
        assertEquals(0, d.eval("x=5"));
    }

    @Test
    void testDivisionEvalZero() {
        Expression e = new Div(new Number(2), new Number(0));
        assertThrows(ArithmeticException.class, () -> e.eval(""));
    }

    @Test
    void testDivisionEvalVariableDenominator() {
        Expression e = new Div(new Variable("x"), new Variable("y"));
        assertEquals(2, e.eval("x=10;y=5"));
    }

    @Test
    void testDivisionWithNegativeVars() {
        Expression e = new Div(new Variable("x"), new Variable("y"));
        assertEquals(-2, e.eval("x=-10;y=5"));
    }
}
