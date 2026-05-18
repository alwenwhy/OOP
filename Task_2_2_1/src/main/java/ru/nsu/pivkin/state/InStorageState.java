package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Состояние "На складе" - пицца готова и ждет курьера.
 */
public class InStorageState implements OrderState {

    @Override
    public void onEnter(Order order) {
        System.out.printf("[Заказ #%d] Передан на склад\n", order.getID());
    }

    @Override
    public void onExit(Order order) {
        System.out.printf("[Заказ #%d] Взят со склада\n", order.getID());
    }

    @Override
    public String getLogMessage() {
        return "На складе";
    }
}