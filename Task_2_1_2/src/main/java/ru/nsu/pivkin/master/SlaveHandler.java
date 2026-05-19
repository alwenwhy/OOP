package ru.nsu.pivkin.master;

import ru.nsu.pivkin.common.Message;
import ru.nsu.pivkin.common.MessageType;
import ru.nsu.pivkin.common.Result;
import ru.nsu.pivkin.common.Task;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Управление соединения с одним слейвом.
 */
public class SlaveHandler {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Создаёт обработчик для уже принятого клиентского сокета.
     *
     * @param socket - подключённый сокет
     */
    public SlaveHandler(Socket socket) throws Exception {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        socket.setSoTimeout(5000);
    }

    /**
     * Отправляет задание слейву и ожидает результата.
     *
     * @param task - задание для выполнения
     * @return - полученный от слейва результат
     */
    public Result sendTask(Task task) throws Exception {
        Message message = new Message(MessageType.TASK, task);

        out.writeObject(message);
        out.flush();

        Message response = (Message) in.readObject();
        return (Result) response.getPayload();
    }

    /**
     * Отправляет слейву сигнал STOP для завершения работы.
     */
    public void stopSlave() throws Exception {
        Message stop = new Message(MessageType.STOP, null);

        out.writeObject(stop);
        out.flush();
    }

    /**
     * Проверяет доступность слейва: отправляет HEARTBEAT и ждёт HEARTBEAT в ответ.
     * Если слейв не ответил за таймаут сокета — выбрасывает исключение.
     *
     * @return - true, если слейв ответил на HEARTBEAT
     */
    public boolean sendHeartbeat() throws Exception {
        Message areYouAlive = new Message(MessageType.HEARTBEAT, null);

        out.writeObject(areYouAlive);
        out.flush();

        Message response = (Message) in.readObject();
        return response.getType() == MessageType.HEARTBEAT;
    }
}
