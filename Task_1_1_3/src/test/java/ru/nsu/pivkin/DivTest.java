package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
