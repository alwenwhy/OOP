package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию умножения двух выражений.
 */
public class Mul implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создаёт выражение умножения двух подвыражений.
     *
     * @param left - левое подвыражение
     * @param right - правое подвыражение
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает произведение.
     *
     * @param vars - Map (пары вида "ключ-значение")
     * @return - значение произведения
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }

    /**
     * Производная выражения умножения.
     * (a * b)' = (a' * b) + (a * b')
     *
     * @param variable - имя переменной
     * @return - возвращает выражение-производную
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }

    /**
     * Для вывода выражения умножения.
     */
    @Override
    public String toString() {
        return "(" + left + "*" + right + ")";
    }
}
