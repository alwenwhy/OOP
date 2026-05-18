package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Состояние "Готовится" - пекарь готовит пиццу.
 */
public class CookingState implements OrderState {
    private final String bakerName;

    public CookingState(String bakerName) {
        this.bakerName = bakerName;
    }

    @Override
    public void onEnter(Order order) {
        System.out.printf("[Заказ #%d] Начало готовки (%s)\n", order.getID(), bakerName);
    }

    @Override
    public void onExit(Order order) {
        System.out.printf("[Заказ #%d] Готовка завершена\n", order.getID());
    }

    @Override
    public String getLogMessage() {
        return "Готовится";
    }
}
