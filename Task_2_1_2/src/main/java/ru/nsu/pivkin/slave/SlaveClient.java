package ru.nsu.pivkin.slave;

import ru.nsu.pivkin.common.Message;
import ru.nsu.pivkin.common.MessageType;
import ru.nsu.pivkin.common.Result;
import ru.nsu.pivkin.common.Task;
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
                Message message = (Message) in.readObject();

                if (message.getType() == MessageType.STOP) {
                    System.out.println("Слейв останавливается");
                    break;
                }

                if (message.getType() == MessageType.HEARTBEAT) {
                    Message iAmAlive = new Message(MessageType.HEARTBEAT, null);
                    out.writeObject(iAmAlive);
                    out.flush();
                }

                if (message.getType() == MessageType.TASK) {
                    Task task = (Task) message.getPayload();

                    boolean hasNonPrime = task
                        .getNumbers()
                        .stream()
                        .anyMatch(number -> !PrimeChecker.isPrime(number));

                    Result result = new Result(task.getTaskID(), hasNonPrime);
                    Message resultMessage = new Message(MessageType.RESULT, result);

                    out.writeObject(resultMessage);
                    out.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
