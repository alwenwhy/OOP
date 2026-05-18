package ru.nsu.pivkin.model;

import ru.nsu.pivkin.state.OrderState;

/**
 * Модель заказа на пиццу с поддержкой паттерна State.
 */
public class Order {
    private final int id;
    private final long startTime;
    private OrderState state;

    /**
     * Конструктор заказа.
     *
     * @param id - идентификатор заказа
     * @param startTime - время создания заказа
     */
    public Order(int id, long startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public int getID() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public OrderState getState() {
        return state;
    }

    /**
     * Изменение состояния заказа.
     *
     * @param newState - новое состояние
     */
    public void setState(OrderState newState) {
        if (this.state != null) {
            this.state.onExit(this);
        }
        this.state = newState;
        if (this.state != null) {
            this.state.onEnter(this);
        }
    }

    @Override
    public String toString() {
        String stateName = state != null ? state.getLogMessage() : "Не определен";
        return "Order{id=" + id + ", state=" + stateName + "}";
    }
}