package ru.nsu.pivkin.service;

import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Pizza;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сервиса склада.
 */
class StorageServiceTest {

    @Test
    void testAddAndTakePizza() {
        StorageService storage = new StorageService(2);
        Pizza pizza = new Pizza(1);

        assertTrue(storage.addPizza(pizza));
        assertEquals(1, storage.size());

        List<Pizza> taken = storage.takePizzas(1);
        assertEquals(1, taken.size());
        assertTrue(storage.isEmpty());
    }

    @Test
    void testStorageCapacity() {
        StorageService storage = new StorageService(2);

        assertTrue(storage.addPizza(new Pizza(1)));
        assertTrue(storage.addPizza(new Pizza(2)));
        assertFalse(storage.addPizza(new Pizza(3)));
    }

    @Test
    void testTakeMoreThanAvailable() {
        StorageService storage = new StorageService(5);
        storage.addPizza(new Pizza(1));
        storage.addPizza(new Pizza(2));

        List<Pizza> taken = storage.takePizzas(5);
        assertEquals(2, taken.size());
        assertTrue(storage.isEmpty());
    }
}