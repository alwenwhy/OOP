package ru.nsu.pivkin.thread;

import ru.nsu.pivkin.model.Pizza;
import ru.nsu.pivkin.service.StorageService;
import java.util.List;

/**
 * Поток курьера. Забирает пиццы со склада и доставляет их.
 */
public class Courier implements Runnable {
    private final String name;
    private final int capacity;
    private final StorageService storage;

    /**
     * Конструктор курьера.
     *
     * @param name имя курьера
     * @param capacity вместимость багажника
     * @param storage склад
     */
    public Courier(String name, int capacity, StorageService storage) {
        this.name = name;
        this.capacity = capacity;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            List<Pizza> pizzas = storage.takePizzas(capacity);

            if (pizzas.isEmpty()) {
                try {
                    Thread.sleep(100);
                    continue;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            deliverPizzas(pizzas);
        }
    }


    /**
     * Выполняет доставку одной или нескольких пицц заказчикам.
     *
     * @param pizzas - список пицц, которые необходимо доставить.
     * Каждая пицца содержит идентификатор соответствующего заказа
     * @throws InterruptedException - если поток курьера был прерван во время
     * имитации процесса доставки
     */
    private void deliverPizzas(List<Pizza> pizzas) {
        int deliveryTime = 2000 + (int)(Math.random() * 2000);

        System.out.printf("[Курьер %s] Забрал %d пицц, доставка %d мс\n",
                name, pizzas.size(), deliveryTime);

        try {
            Thread.sleep(deliveryTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        for (Pizza pizza : pizzas) {
            System.out.printf("[Заказ #%d] Доставлен счастливому заказчику (курьер %s)\n",
                    pizza.getOrderID(), name);
        }
    }
}
