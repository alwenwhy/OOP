package ru.nsu.pivkin.model;

/**
 * Модель готовой пиццы.
 */
public class Pizza {
    private final Order order;
    private final long readyTime;

    /**
     * Конструктор пиццы.
     *
     * @param order заказ, к которому относится пицца
     */
    public Pizza(Order order) {
        this.order = order;
        this.readyTime = System.currentTimeMillis();
    }

    public int getOrderID() {
        return order.getID();
    }

    public Order getOrder() {
        return order;
    }

    public long getReadyTime() {
        return readyTime;
    }
}