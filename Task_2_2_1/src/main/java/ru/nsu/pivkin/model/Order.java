package ru.nsu.pivkin.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Модель заказа на пиццу.
 */
public class Order {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int id;
    private OrderStatus status;
    private long startTime;

    /**
     * Конструктор заказа.
     */
    public Order() {
        this.id = counter.incrementAndGet();
        this.status = OrderStatus.PENDING;
        this.startTime = System.currentTimeMillis();
    }

    public int getID() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", status=" + status + "}";
    }
}