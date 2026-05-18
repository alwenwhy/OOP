package ru.nsu.pivkin.state;

import ru.nsu.pivkin.model.Order;

/**
 * Интерфейс состояния заказа.
 * Паттерн State позволяет изменять поведение заказа в зависимости от статуса.
 */
public interface OrderState {

    /**
     * Действие при переходе в состояние.
     *
     * @param order - заказ
     */
    void onEnter(Order order);

    /**
     * Действие при выходе из состояния.
     *
     * @param order - заказ
     */
    void onExit(Order order);

    /**
     * Возвращает название состояния для вывода в лог.
     */
    String getLogMessage();
}