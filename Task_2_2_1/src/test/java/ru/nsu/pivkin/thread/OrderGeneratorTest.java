package ru.nsu.pivkin.thread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.pivkin.model.Order;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для генератора заказов.
 */
class OrderGeneratorTest {

    private Queue<Order> orderQueue;

    @BeforeEach
    void setUp() {
        orderQueue = new ConcurrentLinkedQueue<>();
    }

    @Test
    void testOrderGenerationProducesOrders() throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(orderQueue, 10);
        Thread t = new Thread(generator);

        t.start();
        Thread.sleep(500);
        t.interrupt();
        t.join(1000);

        assertFalse(orderQueue.isEmpty());
        Order first = orderQueue.peek();
        assertNotNull(first);
        assertTrue(first.getID() > 0);
    }

    @Test
    void testGeneratedOrdersHavePendingState() throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(orderQueue, 20);
        Thread t = new Thread(generator);

        t.start();
        Thread.sleep(300);
        t.interrupt();
        t.join(1000);

        assertFalse(orderQueue.isEmpty());
        Order order = orderQueue.peek();
        assertNotNull(order.getState());
        assertEquals("Ожидает", order.getState().getLogMessage());
    }

    @Test
    void testUniqueOrderIds() throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(orderQueue, 20);
        Thread t = new Thread(generator);

        t.start();
        Thread.sleep(500);
        t.interrupt();
        t.join(1000);

        int count = orderQueue.size();
        assertTrue(count >= 2);

        long uniqueIds = orderQueue.stream().map(Order::getID).distinct().count();
        assertEquals(count, uniqueIds);
    }

    @Test
    void testZeroOrdersPeriod() throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(orderQueue, 0);
        Thread t = new Thread(generator);

        t.start();
        Thread.sleep(200);
        t.interrupt();
        t.join(1000);

        assertFalse(orderQueue.isEmpty());
    }

    @Test
    void testInterruptionStopsGenerator() throws InterruptedException {
        OrderGenerator generator = new OrderGenerator(orderQueue, 10);
        Thread t = new Thread(generator);

        t.start();
        Thread.sleep(200);
        t.interrupt();
        t.join(2000);

        assertFalse(t.isAlive());
    }
}