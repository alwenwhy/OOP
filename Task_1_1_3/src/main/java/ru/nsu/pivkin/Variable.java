package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий переменную в выражении.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Создаёт новую переменную.
     *
     * @param name - имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя переменной, в противном случае кидает ошибку.
     *
     * @param vars - Map (пары вида "ключ-значение")
     * @return - значение константы
     */
    @Override
    public int normalizedEval(Map<String, Integer> vars) {
        if (!vars.containsKey(name))
            throw new IllegalArgumentException("Variable " + name + " not assigned");

        return vars.get(name);
    }

    /**
     * Производная переменной.
     * Если это та переменная, по которой берём производную,
     * возвращаем выражение-константу 1, иначе 0.
     *
     * @param variable - имя переменной
     * @return - возвращает выражение-константу
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(name.equals(variable) ? 1 : 0);
    }

    /**
     * Вывод имени переменной.
     */
    @Override
    public void print() {
        System.out.print(name);
    }
}
