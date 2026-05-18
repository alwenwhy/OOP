package ru.nsu.pivkin.service;

import ru.nsu.pivkin.config.ConfigLoader;
import ru.nsu.pivkin.model.Order;
import ru.nsu.pivkin.model.UnfinishedOrder;
import ru.nsu.pivkin.thread.Baker;
import ru.nsu.pivkin.thread.Courier;
import ru.nsu.pivkin.thread.OrderGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Основной сервис пиццерии.
 * Управляет всеми процессами и потоками.
 */
public class BakeryService {
    private final Queue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private Thread generatorThread;
    private StorageService storage;
    private List<Baker> bakers;
    private List<Courier> couriers;
    private boolean isRunning = true;
    private int workPeriod;

    /**
     * Запуск пиццерии.
     *
     * @param configPath - путь к конфигурационному файлу
     */
    public void start(String configPath) {
        try {
            ConfigLoader loader = new ConfigLoader();
            ConfigLoader.BakeryConfig config = loader.load(configPath);

            storage = new StorageService(config.storageCapacity);
            workPeriod = config.workPeriod;

            bakers = new ArrayList<>();
            for (ConfigLoader.BakerConfig bc : config.bakers) {
                bakers.add(new Baker(bc.name, bc.speed, orderQueue, storage));
            }

            couriers = new ArrayList<>();
            for (ConfigLoader.CourierConfig cc : config.couriers) {
                couriers.add(new Courier(cc.name, cc.capacity, storage));
            }

            System.out.println("=== Пиццерия начинает работу ===");

            startBakers();
            startCouriers();
            startGenerator(config.ordersPeriod);

            waitForWorkComplete();
            pizzeriaShutdown();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Запускаем потоки пекарей.
     */
    private void startBakers() {
        for (Baker baker : bakers) {
            Thread t = new Thread(baker);
            bakerThreads.add(t);
            t.start();
        }
    }

    /**
     * Запускаем потоки курьеров.
     */
    private void startCouriers() {
        for (Courier courier : couriers) {
            Thread t = new Thread(courier);
            courierThreads.add(t);
            t.start();
        }
    }

    /**
     * Запускаем генератор заказов.
     *
     * @param ordersPeriod - количество заказов в секунду
     */
    private void startGenerator(int ordersPeriod) {
        OrderGenerator generator = new OrderGenerator(orderQueue, ordersPeriod);
        generatorThread = new Thread(generator);
        generatorThread.start();
    }

    /**
     * Ожидаем завершения работы.
     */
    private void waitForWorkComplete() {
        try {
            Thread.sleep(workPeriod * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * "Закрываем" пиццерию.
     */
    private void pizzeriaShutdown() {
        System.out.println("\n=== Остановка приема заказов ===");

        if (generatorThread != null) {
            generatorThread.interrupt();
            try {
                generatorThread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("=== Ожидание завершения текущих заказов ===");

        waitOrders();
        sleepTime();
        serializeUnfinishedOrders();

        System.out.println("=== Пиццерия завершила работу ===");
    }

    /**
     * Ожидаем завершение заказов после остановки генерации новых.
     */
    private void waitOrders() {
        int maxWaitSeconds = 30;
        int waited = 0;

        while (waited < maxWaitSeconds) {
            synchronized (orderQueue) {
                boolean hasPending = !orderQueue.isEmpty();

                boolean hasActiveBakers = false;
                for (Baker baker : bakers) {
                    if (baker.isWorking()) {
                        hasActiveBakers = true;
                        break;
                    }
                }

                if (!hasPending && storage.isEmpty() && !hasActiveBakers) {
                    break;
                }
            }

            try {
                Thread.sleep(1000);
                waited++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Отправляем всех работников "спать".
     */
    private void sleepTime() {
        for (Thread t : bakerThreads) {
            t.interrupt();
        }
        for (Thread t : courierThreads) {
            t.interrupt();
        }

        try {
            for (Thread t : bakerThreads) {
                t.join(3000);
            }
            for (Thread t : courierThreads) {
                t.join(3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Сериализация незавершённых заказов.
     */
    private void serializeUnfinishedOrders() {
        List<UnfinishedOrder> unfinished = new ArrayList<>();

        for (Order order : orderQueue) {
            String stateName = order.getState() != null
                    ? order.getState().getLogMessage()
                    : "В очереди";
            unfinished.add(new UnfinishedOrder(
                    order.getID(),
                    stateName,
                    "В очереди",
                    "Нет"
            ));
        }

        for (Baker baker : bakers) {
            Order current = baker.getCurrentOrder();
            if (current != null) {
                String stateName = current.getState() != null
                        ? current.getState().getLogMessage()
                        : "Не определен";
                unfinished.add(new UnfinishedOrder(
                        current.getID(),
                        stateName,
                        baker.getName(),
                        "Нет"
                ));
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("unfinished_orders.ser"))) {
            oos.writeObject(unfinished);
            System.out.println("Сериализовано " + unfinished.size() + " незавершенных заказов");

            System.out.println("\n=== Незавершенные заказы ===");
            for (UnfinishedOrder uo : unfinished) {
                System.out.println(uo);
            }
        } catch (IOException e) {
            System.err.println("Ошибка сериализации: " + e.getMessage());
        }
    }
}