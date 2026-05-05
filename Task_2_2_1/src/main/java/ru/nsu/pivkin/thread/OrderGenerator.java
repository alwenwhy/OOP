package ru.nsu.pivkin.thread;

import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.OrderStatus;
import java.util.Queue;

/**
 * Поток генерации заказов.
 */
public class OrderGenerator implements Runnable {
    private final Queue<Order> orderQueue;
    private final int ordersPeriod;

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
            Order order = new Order();
            order.setStatus(OrderStatus.PENDING);

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
