package ru.nsu.pivkin.service;

import ru.nsu.pivkin.model.Pizza;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Сервис управления складом готовой продукции.
 */
public class StorageService {
    private final Queue<Pizza> storage = new LinkedList<>();
    private final int capacity;

    /**
     * Конструктор склада.
     *
     * @param capacity - максимальная вместимость склада
     */
    public StorageService(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Добавление пиццы на склад.
     *
     * @param pizza - пицца для добавления
     * @return - true, если добавление успешно
     */
    public synchronized boolean addPizza(Pizza pizza) {
        if (storage.size() >= capacity) {
            return false;
        }

        storage.add(pizza);
        System.out.printf("[Заказ #%d] Добавлен на склад\n", pizza.getOrderID());
        notifyAll();

        return true;
    }

    /**
     * Взять пиццу со склада.
     *
     * @param maxCount - максимальное количество для взятия
     * @return - список взятых пицц
     */
    public synchronized List<Pizza> takePizzas(int maxCount) {
        List<Pizza> taken = new ArrayList<>();
        while (storage.isEmpty() && !Thread.currentThread().isInterrupted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return taken;
            }
        }

        while (taken.size() < maxCount && !storage.isEmpty()) {
            Pizza pizza = storage.poll();
            taken.add(pizza);
            System.out.printf("[Заказ #%d] Забран курьером\n", pizza.getOrderID());
        }

        notifyAll();
        return taken;
    }

    /**
     * Проверка пустоты склада.
     */
    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }

    /**
     * Получение текущего размера склада.
     */
    public synchronized int size() {
        return storage.size();
    }
}