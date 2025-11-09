package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию вычитания двух выражений.
 */
public class Sub implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создаёт выражение разности двух подвыражений.
     *
     * @param left - левое подвыражение
     * @param right - правое подвыражение
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает разность.
     *
     * @param vars - Map (пары вида "ключ-значение")
     * @return - значение разности
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }

    /**
     * Производная выражения вычитания.
     * (a - b)' = a' - b'
     *
     * @param variable - имя переменной
     * @return - возвращает разность с производными подвыражений
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Для вывода выражения вычитания.
     */
    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }
}