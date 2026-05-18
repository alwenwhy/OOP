package ru.nsu.pivkin.thread;

import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.state.PendingState;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Поток генерации заказов.
 */
public class OrderGenerator implements Runnable {
    private final Queue<Order> orderQueue;
    private final int ordersPeriod;
    private final AtomicInteger orderCounter = new AtomicInteger(0);

    /**
     * Конструктор генератора заказов.
     *
     * @param orderQueue - очередь заказов
     * @param ordersPeriod - количество заказов в секунду
     */
    public OrderGenerator(Queue<Order> orderQueue, int ordersPeriod) {
        this.orderQueue = orderQueue;
        this.ordersPeriod = ordersPeriod;
    }

    @Override
    public void run() {
        int delayMs = ordersPeriod > 0 ? 1000 / ordersPeriod : 1000;

        while (!Thread.currentThread().isInterrupted()) {
            int orderId = orderCounter.incrementAndGet();
            long startTime = System.currentTimeMillis();
            Order order = new Order(orderId, startTime);

            order.setState(new PendingState());

            synchronized (orderQueue) {
                orderQueue.add(order);
            }

            System.out.printf("[Заказ #%d] Поступил в очередь\n", order.getID());

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
