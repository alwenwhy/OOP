package ru.nsu.pivkin.model;

/**
 * Модель готовой пиццы.
 */
public class Pizza {
    private final int orderID;
    private final long readyTime;

    /**
     * Конструктор пиццы.
     *
     * @param orderID - идентификатор заказа
     */
    public Pizza(int orderID) {
        this.orderID = orderID;
        this.readyTime = System.currentTimeMillis();
    }

    public int getOrderID() {
        return orderID;
    }

    public long getReadyTime() {
        return readyTime;
    }
}