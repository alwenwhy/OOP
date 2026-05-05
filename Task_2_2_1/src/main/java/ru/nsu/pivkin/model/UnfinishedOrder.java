package ru.nsu.pivkin.model;

import java.io.Serializable;

/**
 * Модель незавершенного заказа для сериализации.
 */
public class UnfinishedOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int orderID;
    private final OrderStatus status;
    private final String bakerName;
    private final String courierName;

    /**
     * Конструктор незавершенного заказа.
     */
    public UnfinishedOrder(int orderId, OrderStatus status, String bakerName, String courierName) {
        this.orderID = orderId;
        this.status = status;
        this.bakerName = bakerName;
        this.courierName = courierName;
    }

    @Override
    public String toString() {
        return String.format("Заказ #%d - Статус: %s, Пекарь: %s, Курьер: %s",
                orderID, status.getDescription(), bakerName, courierName);
    }
}