package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию деления двух выражений.
 */
public class Div implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создаёт выражение деление двух подвыражений.
     *
     * @param left - левое подвыражение
     * @param right - правое подвыражение
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает частное.
     *
     * @param vars - Map (пары вида "ключ-значение")
     * @return - значение частного
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) / right.eval(vars);
    }

    /**
     * Производная выражения деления.
     * (a / b)' = (a' * b - a * b') / (b * b)
     *
     * @param variable - имя переменной
     * @return - возвращает выражение-производную
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))
                ),
                new Mul(right, right)
        );
    }

    /**
     * Для вывода выражения деления.
     */
    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }
}
