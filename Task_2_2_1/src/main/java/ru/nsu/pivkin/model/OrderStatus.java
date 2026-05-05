package ru.nsu.pivkin.model;

/**
 * Статусы заказа на пиццу.
 */
public enum OrderStatus {
    PENDING("Ожидает"),
    COOKING("Готовится"),
    IN_STORAGE("На складе"),
    DELIVERING("Доставляется"),
    COMPLETED("Выполнен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}