package ru.nsu.pivkin;

import ru.nsu.pivkin.master.DistributedPrimeChecker;
import ru.nsu.pivkin.master.MasterServer;
import ru.nsu.pivkin.master.SlaveHandler;

/**
 * Точка входа для узла мастера. Запускает MasterServer на порту 5000,
 * ожидает подключения слейвов, затем выполняет распределённую проверку
 * на простоту для предопределённого тестового массива и выводит результат.
 *
 * В конце отправляет сигнал остановки каждому слейву.
 */
public class StartMaster {
    /**
     * Главный метод.
     *
     * @param args - аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            MasterServer server = new MasterServer(5000);
            server.waitForSlaves(1);

            DistributedPrimeChecker slaveClient = new DistributedPrimeChecker(server.getSlaves());

            int[] numbers = {
                    20319251, 6997901, 6997927, 6997937,
                    17858849, 6997967, 6998009, 6998029,
                    6998039, 20165149, 6998051, 6998053
            };

            boolean result = slaveClient.hasNonPrime(numbers);
            System.out.println("Содержит составное: " + (result ? "да" : "нет"));

            for (SlaveHandler slave : server.getSlaves()) {
                slave.stopSlave();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
