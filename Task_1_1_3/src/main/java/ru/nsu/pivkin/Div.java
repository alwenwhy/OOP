package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию деления двух выражений.
 */
public class Div extends Expression {
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
    public int normalizedEval(Map<String, Integer> vars) {
        return left.normalizedEval(vars) / right.normalizedEval(vars);
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
     * Вывод выражения деления.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }
}
