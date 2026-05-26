package ru.nsu.pivkin.common;

import java.io.Serializable;

/**
 * Результат проверки на простоту, отправляемый слейвом обратно мастеру.
 */
public class Result implements Serializable {
    private final int taskID;
    private final boolean hasNonPrime;

    /**
     * Создаёт новый результат.
     *
     * @param taskID - идентификатор задания
     * @param hasNonPrime - true, если в списке найдено составное число
     */
    public Result(int taskID, boolean hasNonPrime) {
        this.taskID = taskID;
        this.hasNonPrime = hasNonPrime;
    }

    /**
     * Возвращает идентификатор задания.
     *
     * @return - идентификатор задания
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Возвращает, содержал ли список данных слейва хотя бы одно составное число.
     *
     * @return - true, если найдено составное число
     */
    public boolean hasNonPrime() {
        return hasNonPrime;
    }
}
