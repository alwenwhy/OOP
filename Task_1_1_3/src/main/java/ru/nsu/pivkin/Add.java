package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию сложения двух выражений.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Создаёт выражение сложения двух подвыражений.
     *
     * @param left - левое подвыражение
     * @param right - правое подвыражение
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает сумму.
     *
     * @param vars - Map (пары вида "ключ-значение")
     * @return - значение суммы
     */
    @Override
    public int normalizedEval(Map<String, Integer> vars) {
        return left.normalizedEval(vars) + right.normalizedEval(vars);
    }

    /**
     * Производная выражении сложения.
     * (a + b)' = a' + b'
     *
     * @param variable - имя переменной
     * @return - возвращает сумму с производными подвыражений
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Вывод выражения сложения.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }
}
