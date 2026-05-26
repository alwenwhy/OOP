package ru.nsu.pivkin;

import ru.nsu.pivkin.slave.SlaveClient;

/**
 * Точка входа для слейв-узла. Подключается к мастеру по адресу localhost:5000
 * и начинает обрабатывать задания до получения сигнала остановки.
 */
public class StartSlave {
    /**
     * Главный метод.
     *
     * @param args - аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SlaveClient slave = new SlaveClient("localhost", 5000);
        slave.start();
    }
}
