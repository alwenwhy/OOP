package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}