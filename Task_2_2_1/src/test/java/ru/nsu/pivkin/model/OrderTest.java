package ru.nsu.pivkin.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для модели заказа.
 */
class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order();
        assertNotNull(order.getID());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testOrderStatusChange() {
        Order order = new Order();
        order.setStatus(OrderStatus.COOKING);
        assertEquals(OrderStatus.COOKING, order.getStatus());
    }

    @Test
    void testOrderIdsAreUnique() {
        Order order1 = new Order();
        Order order2 = new Order();
        assertNotEquals(order1.getID(), order2.getID());
    }
}
