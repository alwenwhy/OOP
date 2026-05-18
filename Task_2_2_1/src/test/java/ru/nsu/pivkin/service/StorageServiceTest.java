package ru.nsu.pivkin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.Pizza;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сервиса склада.
 */
class StorageServiceTest {

    private StorageService storage;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        storage = new StorageService(3);
        testOrder = new Order(1, System.currentTimeMillis());
    }

    private Pizza createPizza(int orderId) {
        Order order = new Order(orderId, System.currentTimeMillis());
        return new Pizza(order);
    }

    @Test
    void testAddPizzaSuccess() {
        Pizza pizza = new Pizza(testOrder);
        assertTrue(storage.addPizza(pizza));
        assertEquals(1, storage.size());
    }

    @Test
    void testAddPizzaWhenFull() {
        storage.addPizza(createPizza(1));
        storage.addPizza(createPizza(2));
        storage.addPizza(createPizza(3));

        Pizza pizza4 = createPizza(4);
        assertFalse(storage.addPizza(pizza4));
        assertEquals(3, storage.size());
    }

    @Test
    void testTakePizzas() {
        storage.addPizza(createPizza(1));
        storage.addPizza(createPizza(2));

        List<Pizza> taken = storage.takePizzas(2);
        assertEquals(2, taken.size());
        assertTrue(storage.isEmpty());
    }

    @Test
    void testTakeMoreThanAvailable() {
        storage.addPizza(createPizza(1));
        storage.addPizza(createPizza(2));

        List<Pizza> taken = storage.takePizzas(5);
        assertEquals(2, taken.size());
        assertTrue(storage.isEmpty());
    }

    @Test
    void testAddAndTakeMultipleTimes() {
        for (int i = 1; i <= 3; i++) {
            storage.addPizza(createPizza(i));
        }

        List<Pizza> taken1 = storage.takePizzas(2);
        assertEquals(2, taken1.size());
        assertEquals(1, storage.size());

        storage.addPizza(createPizza(4));
        assertEquals(2, storage.size());

        List<Pizza> taken2 = storage.takePizzas(2);
        assertEquals(2, taken2.size());
        assertTrue(storage.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(storage.isEmpty());
        storage.addPizza(new Pizza(testOrder));
        assertFalse(storage.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, storage.size());
        storage.addPizza(new Pizza(testOrder));
        assertEquals(1, storage.size());
        storage.addPizza(createPizza(2));
        assertEquals(2, storage.size());
    }
}