package ru.nsu.pivkin.state;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Order;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для паттерна State.
 */
class StateTest {

    @Test
    void testPendingState() {
        PendingState state = new PendingState();
        Order order = new Order(1, System.currentTimeMillis());

        assertEquals("Ожидает", state.getLogMessage());

        assertDoesNotThrow(() -> state.onEnter(order));
        assertDoesNotThrow(() -> state.onExit(order));
    }

    @Test
    void testCookingState() {
        CookingState state = new CookingState("TestBaker");
        Order order = new Order(1, System.currentTimeMillis());

        assertEquals("Готовится", state.getLogMessage());
        assertDoesNotThrow(() -> state.onEnter(order));
        assertDoesNotThrow(() -> state.onExit(order));
    }

    @Test
    void testInStorageState() {
        InStorageState state = new InStorageState();
        Order order = new Order(1, System.currentTimeMillis());

        assertEquals("На складе", state.getLogMessage());
        assertDoesNotThrow(() -> state.onEnter(order));
        assertDoesNotThrow(() -> state.onExit(order));
    }

    @Test
    void testDeliveringState() {
        DeliveringState state = new DeliveringState("TestCourier");
        Order order = new Order(1, System.currentTimeMillis());

        assertEquals("Доставляется", state.getLogMessage());
        assertDoesNotThrow(() -> state.onEnter(order));
        assertDoesNotThrow(() -> state.onExit(order));
    }

    @Test
    void testCompletedState() {
        CompletedState state = new CompletedState();
        Order order = new Order(1, System.currentTimeMillis());

        assertEquals("Выполнен", state.getLogMessage());
        assertDoesNotThrow(() -> state.onEnter(order));
        assertDoesNotThrow(() -> state.onExit(order));
    }
}
