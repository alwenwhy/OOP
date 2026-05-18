package ru.nsu.pivkin.thread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.Pizza;
import ru.nsu.pivkin.service.StorageService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для курьера.
 */
class CourierTest {

    private StorageService storage;

    @BeforeEach
    void setUp() {
        storage = new StorageService(10);
    }

    private Pizza createPizza(int orderId) {
        Order order = new Order(orderId, System.currentTimeMillis());
        return new Pizza(order);
    }

    @Test
    void testCourierCreation() {
        Courier courier = new Courier("Тест-курьер", 3, storage);
        assertNotNull(courier);
    }

    @Test
    void testCourierTakesPizzasFromStorage() throws InterruptedException {
        storage.addPizza(createPizza(1));
        storage.addPizza(createPizza(2));

        Courier courier = new Courier("Тест-курьер", 3, storage);
        Thread t = new Thread(courier);

        t.start();
        Thread.sleep(300);

        assertTrue(storage.isEmpty());

        t.interrupt();
        t.join(3000);
    }

    @Test
    void testCourierWaitsWhenStorageEmpty() throws InterruptedException {
        Courier courier = new Courier("Тест-курьер", 2, storage);
        Thread t = new Thread(courier);

        t.start();
        Thread.sleep(200);
        assertTrue(t.isAlive());

        t.interrupt();
        t.join(2000);
        assertFalse(t.isAlive());
    }

    @Test
    void testCourierLimitedByCapacity() throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            storage.addPizza(createPizza(i));
        }

        Courier courier = new Courier("Тест-курьер", 2, storage);
        Thread t = new Thread(courier);

        t.start();
        Thread.sleep(200);

        assertEquals(3, storage.size());

        t.interrupt();
        t.join(3000);
    }

    @Test
    void testCourierInterruption() throws InterruptedException {
        Courier courier = new Courier("Тест-курьер", 1, storage);
        Thread t = new Thread(courier);

        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join(3000);

        assertFalse(t.isAlive());
    }

    @Test
    void testCourierDeliversAndStops() throws InterruptedException {
        storage.addPizza(createPizza(42));
        Courier courier = new Courier("Тест-курьер", 1, storage);

        Thread t = new Thread(courier);
        t.start();
        Thread.sleep(300);

        assertTrue(storage.isEmpty());

        t.interrupt();
        t.join(5000);
        assertFalse(t.isAlive());
    }

    @Test
    void testCourierCompletesFullDelivery() throws InterruptedException {
        storage.addPizza(createPizza(1));
        storage.addPizza(createPizza(2));

        Courier courier = new Courier("Тест-курьер", 3, storage);
        Thread t = new Thread(courier);

        t.start();
        Thread.sleep(4500);

        assertTrue(storage.isEmpty());

        t.interrupt();
        t.join(3000);
        assertFalse(t.isAlive());
    }
}