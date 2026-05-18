package ru.nsu.pivkin.thread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.service.StorageService;
import ru.nsu.pivkin.state.PendingState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для пекаря.
 */
class BakerTest {

    private Queue<Order> orderQueue;
    private StorageService storage;

    @BeforeEach
    void setUp() {
        orderQueue = new ConcurrentLinkedQueue<>();
        storage = new StorageService(5);
    }

    private Order createOrder(int id) {
        Order order = new Order(id, System.currentTimeMillis());
        order.setState(new PendingState());
        return order;
    }

    @Test
    void testBakerCreation() {
        Baker baker = new Baker("Тест-пекарь", 100, orderQueue, storage);

        assertEquals("Тест-пекарь", baker.getName());
        assertNull(baker.getCurrentOrder());
        assertFalse(baker.isWorking());
    }

    @Test
    void testBakerProcessesOrder() throws InterruptedException {
        orderQueue.add(createOrder(1));
        Baker baker = new Baker("Тест-пекарь", 100, orderQueue, storage);

        Thread t = new Thread(baker);
        t.start();
        Thread.sleep(500);
        t.interrupt();
        t.join(2000);

        assertTrue(orderQueue.isEmpty());
        assertEquals(1, storage.size());
    }

    @Test
    void testBakerWaitsWhenQueueEmpty() throws InterruptedException {
        Baker baker = new Baker("Тест-пекарь", 100, orderQueue, storage);

        Thread t = new Thread(baker);
        t.start();
        Thread.sleep(300);
        assertTrue(t.isAlive());

        t.interrupt();
        t.join(1000);
        assertFalse(t.isAlive());
    }

    @Test
    void testBakerProcessesMultipleOrders() throws InterruptedException {
        orderQueue.add(createOrder(1));
        orderQueue.add(createOrder(2));
        orderQueue.add(createOrder(3));

        Baker baker = new Baker("Тест-пекарь", 50, orderQueue, storage);

        Thread t = new Thread(baker);
        t.start();
        Thread.sleep(800);
        t.interrupt();
        t.join(2000);

        assertTrue(orderQueue.isEmpty());
        assertEquals(3, storage.size());
    }

    @Test
    void testBakerWaitsWhenStorageFull() throws InterruptedException {
        StorageService smallStorage = new StorageService(1);
        orderQueue.add(createOrder(1));
        orderQueue.add(createOrder(2));

        Baker baker = new Baker("Тест-пекарь", 50, orderQueue, smallStorage);

        Thread t = new Thread(baker);
        t.start();
        Thread.sleep(400);

        assertEquals(1, smallStorage.size());

        t.interrupt();
        t.join(2000);
    }

    @Test
    void testBakerInterruptionStops() throws InterruptedException {
        Baker baker = new Baker("Тест-пекарь", 1000, orderQueue, storage);
        Thread t = new Thread(baker);

        t.start();
        Thread.sleep(100);
        t.interrupt();
        t.join(2000);

        assertFalse(t.isAlive());
    }

    @Test
    void testGetCurrentOrderDuringCooking() throws InterruptedException {
        orderQueue.add(createOrder(7));
        Baker baker = new Baker("Тест-пекарь", 500, orderQueue, storage);

        Thread t = new Thread(baker);
        t.start();
        Thread.sleep(150);

        Order current = baker.getCurrentOrder();
        assertNotNull(current);
        assertEquals(7, current.getID());
        assertTrue(baker.isWorking());

        t.interrupt();
        t.join(2000);
    }
}