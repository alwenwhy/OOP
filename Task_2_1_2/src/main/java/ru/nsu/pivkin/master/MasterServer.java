package ru.nsu.pivkin.master;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP-сервер, принимающий входящие подключения от слейв-узлов.
 * Топология: один мастер, N слейвов. Мастер инициирует все запросы
 * вычислений, слейвы являются пассивными исполнителями.
 */
public class MasterServer {
    private final int port;
    private final List<SlaveHandler> slaves = new ArrayList<>();

    /**
     * Создаёт сервер мастера, который будет слушать на заданном порту.
     *
     * @param port - TCP-порт для привязки
     */
    public MasterServer(int port) {
        this.port = port;
    }

    /**
     * Принимает ровно slavesNums подключений от слейвов, затем возвращает управление.
     *
     * @param slavesNums - количество слейв-узлов для ожидания
     */
    public void waitForSlaves(int slavesNums) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Ожидание подключения слейвов...");

        while (slaves.size() < slavesNums) {
            Socket socket = serverSocket.accept();
            SlaveHandler worker = new SlaveHandler(socket);
            slaves.add(worker);
            System.out.println("Слейв подключён: " + slaves.size());
        }
    }

    /**
     * Возвращает список подключённых обработчиков слейвов.
     * Следует вызывать только после завершения waitForWorkers.
     *
     * @return - список подключённых слейвов
     */
    public List<SlaveHandler> getSlaves() {
        return slaves;
    }
}
