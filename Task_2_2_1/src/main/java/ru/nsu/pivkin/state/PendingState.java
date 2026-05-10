package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Состояние "Ожидает" - заказ поступил, но еще не взят пекарем.
 */
public class PendingState implements OrderState {

    @Override
    public void onEnter(Order order) {
        System.out.printf("[Заказ #%d] Поступил в систему\n", order.getID());
    }

    @Override
    public void onExit(Order order) {
        System.out.printf("[Заказ #%d] Взят в работу\n", order.getID());
    }

    @Override
    public String getLogMessage() {
        return "Ожидает";
    }
}