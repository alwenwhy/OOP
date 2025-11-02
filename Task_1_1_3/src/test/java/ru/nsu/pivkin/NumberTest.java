package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumberTest {
    @Test
    void testEvalReturnsValue() {
        Expression num = new Number(5);
        assertEquals(5, num.eval(""));
    }

    @Test
    void testDerivativeIsZero() {
        Expression num = new Number(5);
        Expression d = num.derivative("x");
        assertEquals(0, d.eval("x=10"));
    }
}
