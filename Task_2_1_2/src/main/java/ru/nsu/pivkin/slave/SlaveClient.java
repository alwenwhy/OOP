package ru.nsu.pivkin.slave;

import ru.nsu.pivkin.common.HeartbeatMessage;
import ru.nsu.pivkin.common.Result;
import ru.nsu.pivkin.common.ResultMessage;
import ru.nsu.pivkin.common.StopMessage;
import ru.nsu.pivkin.common.TaskMessage;
import ru.nsu.pivkin.util.PrimeChecker;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Клиент слейв-узла: подключается к мастеру и обрабатывает задания.
 * Слейв работает в одном потоке и обрабатывает задания последовательно.
 */
public class SlaveClient {
    private final String host;
    private final int port;

    /**
     * Создаёт слейв-клиент, который будет подключаться к указанному адресу мастера.
     *
     * @param host - имя хоста или IP-адрес мастера
     * @param port - TCP-порт, на котором слушает мастер
     */
    public SlaveClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Подключается к мастеру и запускает цикл обработки заданий.
     */
    public void start() {
        try (
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Подключён к мастеру");

            while (true) {
                Object message = in.readObject();

                if (message instanceof StopMessage) {
                    System.out.println("Слейв останавливается");
                    break;
                }

                if (message instanceof HeartbeatMessage) {
                    out.writeObject(new HeartbeatMessage());
                    out.flush();
                }

                if (message instanceof TaskMessage taskMessage) {
                    boolean hasNonPrime = taskMessage.getTask()
                        .getNumbers()
                        .stream()
                        .anyMatch(number -> !PrimeChecker.isPrime(number));

                    Result result = new Result(taskMessage.getTask().getTaskID(), hasNonPrime);
                    out.writeObject(new ResultMessage(result));
                    out.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
