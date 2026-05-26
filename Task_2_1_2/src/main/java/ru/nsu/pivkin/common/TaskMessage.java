package ru.nsu.pivkin.common;

import java.io.Serializable;

/**
 * Сообщение с заданием, отправляемое мастером слейву.
 */
public class TaskMessage implements Serializable {
    private final Task task;

    /**
     * Создаёт сообщение с заданием.
     *
     * @param task - задание для выполнения
     */
    public TaskMessage(Task task) {
        this.task = task;
    }

    /**
     * Возвращает задание.
     *
     * @return - задание
     */
    public Task getTask() {
        return task;
    }
}
