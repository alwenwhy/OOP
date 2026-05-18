package ru.nsu.pivkin;

import ru.nsu.pivkin.service.BakeryService;

/**
 * Главный класс. Запускает симулятор пиццерии.
 */
public class Main {
    /**
     * Точка входа в приложение.
     *
     * @param args - аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        String config = args.length > 0 ? args[0] : "src/main/resources/config.json";

        BakeryService bakery = new BakeryService();
        bakery.start(config);
    }
}
