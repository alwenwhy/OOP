package ru.nsu.pivkin.model;

import java.io.Serializable;

/**
 * Модель незавершенного заказа для сериализации.
 */
public class UnfinishedOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int orderID;
    private final String status;
    private final String bakerName;
    private final String courierName;

    /**
     * Конструктор незавершенного заказа.
     *
     * @param orderID - идентификатор заказа
     * @param status - статус заказа (строка)
     * @param bakerName - имя пекаря
     * @param courierName - имя курьера
     */
    public UnfinishedOrder(int orderID, String status, String bakerName, String courierName) {
        this.orderID = orderID;
        this.status = status;
        this.bakerName = bakerName;
        this.courierName = courierName;
    }

    @Override
    public String toString() {
        return String.format("Заказ #%d - Статус: %s, Пекарь: %s, Курьер: %s",
                orderID, status, bakerName, courierName);
    }
}