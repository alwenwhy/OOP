package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Состояние "Доставляется" - курьер везет пиццу.
 */
public class DeliveringState implements OrderState {
    private final String courierName;

    public DeliveringState(String courierName) {
        this.courierName = courierName;
    }

    @Override
    public void onEnter(Order order) {
        System.out.printf("[Заказ #%d] В пути (курьер %s)\n", order.getID(), courierName);
    }

    @Override
    public void onExit(Order order) {
        System.out.printf("[Заказ #%d] Доставлен\n", order.getID());
    }

    @Override
    public String getLogMessage() {
        return "Доставляется";
    }
}