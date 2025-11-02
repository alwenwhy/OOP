package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MulTest {
    @Test
    void testMultiplicationEval() {
        Expression e = new Mul(new Number(3), new Variable("x"));
        assertEquals(15, e.eval("x=5"));
    }

    @Test
    void testDerivativeOfProduct() {
        Expression e = new Mul(new Variable("x"), new Variable("x"));
        Expression d = e.derivative("x");
        assertEquals(10, d.eval("x=5")); // 2*x = 10
    }
}
