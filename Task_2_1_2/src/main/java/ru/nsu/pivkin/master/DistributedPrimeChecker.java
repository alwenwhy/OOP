package ru.nsu.pivkin.master;

import ru.nsu.pivkin.common.Result;
import ru.nsu.pivkin.common.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Распределяет задачу проверки на простоту по пулу слейв-узлов.
 */
public class DistributedPrimeChecker {
    private final List<SlaveHandler> slaves;

    /**
     * Создаёт проверяющий объект, опирающийся на заданный список слейвов.
     *
     * @param slaves - список подключённых слейв-обработчиков
     */
    public DistributedPrimeChecker(List<SlaveHandler> slaves) {
        this.slaves = slaves;
    }

    /**
     * Проверяет, содержит ли массив numbs хотя бы одно не простое число.
     *
     * @param nums - массив для проверки
     * @return - true, если хотя бы один элемент массива составной
     */
    public boolean hasNonPrime(int[] nums) throws Exception {
        int partSize = (int) Math.ceil((double) nums.length / slaves.size());

        List<Task> tasks = new ArrayList<>();
        int taskID = 0;

        for (int i = 0; i < nums.length; i += partSize) {
            List<Integer> part = new ArrayList<>();
            for (int j = i; j < Math.min(i + partSize, nums.length); j++) {
                part.add(nums[j]);
            }

            tasks.add(new Task(taskID++, part));
        }

        for (int i = 0; i < tasks.size(); i++) {
            Result result = slaves.get(i).sendTask(tasks.get(i));

            if (result.hasNonPrime()) {
                return true;
            }
        }

        return false;
    }
}
