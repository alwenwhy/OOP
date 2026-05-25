package ru.nsu.pivkin.master;

import ru.nsu.pivkin.common.HeartbeatMessage;
import ru.nsu.pivkin.common.ResultMessage;
import ru.nsu.pivkin.common.StopMessage;
import ru.nsu.pivkin.common.Task;
import ru.nsu.pivkin.common.TaskMessage;
import ru.nsu.pivkin.common.Result;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Управляет соединением с одним слейвом.
 */
public class SlaveHandler implements AutoCloseable {
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
        out.writeObject(new TaskMessage(task));
        out.flush();

        ResultMessage response = (ResultMessage) in.readObject();
        return response.getResult();
    }

    /**
     * Отправляет слейву сигнал StopMessage для завершения работы.
     */
    public void stopSlave() throws Exception {
        out.writeObject(new StopMessage());
        out.flush();
    }

    /**
     * Проверяет доступность слейва: отправляет HeartbeatMessage и ждёт ответа.
     * Если слейв не ответил за таймаут сокета - выбрасывает исключение.
     *
     * @return - true, если слейв ответил на HeartbeatMessage
     */
    public boolean sendHeartbeat() throws Exception {
        out.writeObject(new HeartbeatMessage());
        out.flush();

        Object response = in.readObject();
        return response instanceof HeartbeatMessage;
    }

    /**
     * Закрывает сокет и потоки соединения со слейвом.
     */
    @Override
    public void close() throws Exception {
        socket.close();
    }
}
