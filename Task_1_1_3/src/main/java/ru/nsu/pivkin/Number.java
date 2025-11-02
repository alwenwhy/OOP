package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий числовую константу в выражении.
 */
public class Number extends Expression {
    private final int value;

    /**
     * Создаёт новое выражение-константу.
     *
     * @param value - числовое (целочисленное) значение
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Возвращает значение константы.
     *
     * @param vars - Map (пары вида "ключ-значение", не используется)
     * @return - значение константы
     */
    @Override
    public int normalizedEval(Map<String, Integer> vars) {
        return value;
    }

    /**
     * Производная константы это 0.
     *
     * @param variable - имя переменной (не используется)
     * @return возвращает выражение-константу равную 0
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Вывод значения константы.
     */
    @Override
    public void print() {
        System.out.print(value);
    }
}

