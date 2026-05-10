package ru.nsu.pivkin.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.state.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для модели заказа с паттерном State.
 */
class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1, System.currentTimeMillis());
    }

    @Test
    void testOrderCreation() {
        assertEquals(1, order.getID());
        assertTrue(order.getStartTime() > 0);
        assertNull(order.getState());
    }

    @Test
    void testSetPendingState() {
        order.setState(new PendingState());
        assertNotNull(order.getState());
        assertEquals("Ожидает", order.getState().getLogMessage());
    }

    @Test
    void testSetCookingState() {
        order.setState(new CookingState("Тест-пекарь"));
        assertNotNull(order.getState());
        assertEquals("Готовится", order.getState().getLogMessage());
    }

    @Test
    void testSetInStorageState() {
        order.setState(new InStorageState());
        assertEquals("На складе", order.getState().getLogMessage());
    }

    @Test
    void testSetDeliveringState() {
        order.setState(new DeliveringState("Тест-курьер"));
        assertEquals("Доставляется", order.getState().getLogMessage());
    }

    @Test
    void testSetCompletedState() {
        order.setState(new CompletedState());
        assertEquals("Выполнен", order.getState().getLogMessage());
    }

    @Test
    void testStateTransition() {
        order.setState(new PendingState());
        assertEquals("Ожидает", order.getState().getLogMessage());

        order.setState(new CookingState("Пекарь"));
        assertEquals("Готовится", order.getState().getLogMessage());

        order.setState(new InStorageState());
        assertEquals("На складе", order.getState().getLogMessage());

        order.setState(new DeliveringState("Курьер"));
        assertEquals("Доставляется", order.getState().getLogMessage());

        order.setState(new CompletedState());
        assertEquals("Выполнен", order.getState().getLogMessage());
    }

    @Test
    void testToString() {
        order.setState(new CookingState("Пекарь"));
        String str = order.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("Готовится"));
    }
}
