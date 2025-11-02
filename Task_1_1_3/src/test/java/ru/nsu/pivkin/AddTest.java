package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddTest {
    @Test
    void testAdditionNums() {
        Expression e = new Add(new Number(3), new Number(42));
        assertEquals(45, e.eval(""));
    }

    @Test
    void testAdditionEval() {
        Expression e = new Add(new Number(3), new Variable("x"));
        assertEquals(8, e.eval("x=5"));
    }

    @Test
    void testDerivativeOfAddition() {
        Expression e = new Add(new Variable("x"), new Variable("x"));
        Expression d = e.derivative("x");
        assertEquals(2, d.eval("x=0")); // (1+1)
    }
}
