package ru.nsu.pivkin.thread;

import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.Pizza;
import ru.nsu.pivkin.service.StorageService;
import ru.nsu.pivkin.state.CookingState;
import ru.nsu.pivkin.state.InStorageState;
import java.util.Queue;

/**
 * Поток пекаря. Готовит пиццы и передает их на склад.
 */
public class Baker implements Runnable {
    private final String name;
    private final int cookingTime;
    private final Queue<Order> orderQueue;
    private final StorageService storage;
    private Order currentOrder;
    private boolean isWorking = false;

    /**
     * Конструктор пекаря.
     *
     * @param name - имя пекаря
     * @param cookingTime - время приготовления в мс
     * @param orderQueue - очередь заказов
     * @param storage - склад
     */
    public Baker(String name, int cookingTime, Queue<Order> orderQueue, StorageService storage) {
        this.name = name;
        this.cookingTime = cookingTime;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    /**
     * Возвращает текущий заказ пекаря.
     *
     * @return - текущий заказ
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Возвращает имя пекаря.
     *
     * @return - имя пекаря
     */
    public String getName() {
        return name;
    }

    /**
     * Работает ли пекарь над заказом.
     *
     * @return - true если пекарь занят
     */
    public boolean isWorking() {
        return isWorking;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            currentOrder = null;

            synchronized (orderQueue) {
                currentOrder = orderQueue.poll();
            }

            if (currentOrder == null) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            processOrder(currentOrder);
        }
    }

    private void processOrder(Order order) {
        isWorking = true;

        order.setState(new CookingState(name));
        System.out.printf("[Заказ #%d] Начало готовки (%s, %d мс)\n",
                order.getID(), name, cookingTime);

        try {
            Thread.sleep(cookingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            isWorking = false;
            return;
        }

        order.setState(new InStorageState());
        Pizza pizza = new Pizza(order);

        while (!storage.addPizza(pizza) && !Thread.currentThread().isInterrupted()) {
            System.out.printf("[Заказ #%d] Склад полон, ожидание (%s)\n", order.getID(), name);
            try {
                synchronized (storage) {
                    storage.wait(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        isWorking = false;
    }
}
