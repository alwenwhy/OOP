package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Sub.
 */
public class SubTest {
    @Test
    void testSubtractionEval() {
        Expression e = new Sub(new Number(10), new Variable("x"));
        assertEquals(6, e.eval("x=4"));
    }

    @Test
    void testDerivativeOfSubtraction() {
        Expression e = new Sub(new Variable("x"), new Number(3));
        Expression d = e.derivative("x");
        assertEquals(1, d.eval("x=0"));
    }

    @Test
    void testSubtractionDerivativeVariableMinusConstant() {
        Expression e = new Sub(new Variable("x"), new Number(3));
        Expression d = e.derivative("x");
        assertEquals(1, d.eval(""));
    }

    @Test
    void testSubtractionDerivativeConstantMinusVariable() {
        Expression e = new Sub(new Number(5), new Variable("x"));
        Expression d = e.derivative("x");
        assertEquals(-1, d.eval(""));
    }

    @Test
    void testSubtractionDerivativeWithVariables() {
        Expression e = new Sub(new Variable("x"), new Variable("y"));
        Expression d = e.derivative("x");
        assertEquals(1, d.eval("x=5;y=3"));
    }

    @Test
    void testPrintFormat() {
        Expression e = new Sub(new Variable("a"), new Variable("b"));
        assertDoesNotThrow(e::print);
    }
}