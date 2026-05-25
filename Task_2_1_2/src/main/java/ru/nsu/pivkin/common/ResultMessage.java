package ru.nsu.pivkin.common;

import java.io.Serializable;

/**
 * Сообщение с результатом, отправляемое слейвом мастеру.
 */
public class ResultMessage implements Serializable {
    private final Result result;

    /**
     * Создаёт сообщение с результатом.
     *
     * @param result - результат проверки на простоту
     */
    public ResultMessage(Result result) {
        this.result = result;
    }

    /**
     * Возвращает результат.
     *
     * @return - результат проверки
     */
    public Result getResult() {
        return result;
    }
}
