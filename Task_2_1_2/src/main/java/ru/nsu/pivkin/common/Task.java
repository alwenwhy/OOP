package ru.nsu.pivkin.common;

import java.io.Serializable;
import java.util.List;

/**
 * "Единица работы", отправляемая от мастера к слейву. Каждое задание содержит уникальный
 * идентификатор и непустой список целых чисел, которые нужно проверить на простоту.
 */
public class Task implements Serializable {
    private final int taskID;
    private final List<Integer> nums;

    /**
     * Создаёт новое задание.
     *
     * @param taskID -  неотрицательный идентификатор, уникальный
     * @param nums - список целых чисел для проверки
     */
    public Task(int taskID, List<Integer> nums) {
        this.taskID = taskID;
        this.nums = nums;
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
     * Возвращает список целых чисел, назначенных данному заданию.
     *
     * @return - числа для проверки
     */
    public List<Integer> getNumbers() {
        return nums;
    }
}
