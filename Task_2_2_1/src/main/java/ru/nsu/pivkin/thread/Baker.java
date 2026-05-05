package ru.nsu.pivkin.thread;

import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.OrderStatus;
import ru.nsu.pivkin.model.Pizza;
import ru.nsu.pivkin.service.StorageService;
import java.util.Queue;

/**
 * Поток пекаря.
 * Готовит пиццы и передает их на склад.
 */
public class Baker implements Runnable {
    private final String name;
    private final int cookingPeriod;
    private final Queue<Order> orderQueue;
    private final StorageService storage;
    private Order currentOrder;
    private boolean isWorking = false;

    /**
     * Конструктор пекаря.
     *
     * @param name - имя пекаря
     * @param cookingPeriod - время приготовления в мс
     * @param orderQueue - очередь заказов
     * @param storage - склад
     */
    public Baker(String name, int cookingPeriod, Queue<Order> orderQueue, StorageService storage) {
        this.name = name;
        this.cookingPeriod = cookingPeriod;
        this.orderQueue = orderQueue;
        this.storage = storage;
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

    /**
     * Возвращает текущий заказ пекаря.
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Возвращает имя пекаря.
     */
    public String getName() {
        return name;
    }

    /**
     * Работает ли пекарь над заказом.
     */
    public boolean isWorking() {
        return isWorking;
    }


    /**
     * Обрабатывает полный цикл приготовления заказа.
     *
     * @param order - заказ, который необходимо приготовить
     * @throws InterruptedException - если поток пекаря был прерван во время
     * ожидания (при приготовлении или при попытке положить пиццу на склад)
     */
    private void processOrder(Order order) {
        isWorking = true;
        order.setStatus(OrderStatus.COOKING);
        System.out.printf("[Заказ #%d] Начало готовки (%s, %d мс)\n",
                order.getID(), name, cookingPeriod);

        try {
            Thread.sleep(cookingPeriod);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            isWorking = false;

            return;
        }

        order.setStatus(OrderStatus.IN_STORAGE);
        Pizza pizza = new Pizza(order.getID());

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
