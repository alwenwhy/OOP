package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Состояние "Выполнен" - заказ завершен.
 */
public class CompletedState implements OrderState {

    @Override
    public void onEnter(Order order) {
        System.out.printf("[Заказ #%d] Заказ выполнен\n", order.getID());
    }

    @Override
    public void onExit(Order order) {
        // ничего не делаем.
    }

    @Override
    public String getLogMessage() {
        return "Выполнен";
    }
}
