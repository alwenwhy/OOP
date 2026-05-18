package ru.nsu.pivkin.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для модели пиццы.
 */
class PizzaTest {

    private Order order;
    private Pizza pizza;

    @BeforeEach
    void setUp() {
        order = new Order(42, System.currentTimeMillis());
        pizza = new Pizza(order);
    }

    @Test
    void testPizzaCreation() {
        assertNotNull(pizza);
        assertEquals(42, pizza.getOrderID());
    }

    @Test
    void testGetOrder() {
        assertSame(order, pizza.getOrder());
        assertEquals(order.getID(), pizza.getOrder().getID());
    }

    @Test
    void testReadyTimeIsSet() {
        long before = System.currentTimeMillis();
        Pizza newPizza = new Pizza(order);
        long after = System.currentTimeMillis();

        assertTrue(newPizza.getReadyTime() >= before);
        assertTrue(newPizza.getReadyTime() <= after);
    }

    @Test
    void testGetOrderId() {
        Order order2 = new Order(100, System.currentTimeMillis());
        Pizza pizza2 = new Pizza(order2);

        assertEquals(100, pizza2.getOrderID());
    }

    @Test
    void testMultiplePizzasForDifferentOrders() {
        Order order1 = new Order(1, System.currentTimeMillis());
        Order order2 = new Order(2, System.currentTimeMillis());

        Pizza pizza1 = new Pizza(order1);
        Pizza pizza2 = new Pizza(order2);

        assertNotEquals(pizza1.getOrderID(), pizza2.getOrderID());
        assertNotSame(pizza1.getOrder(), pizza2.getOrder());
    }
}