package ru.nsu.pivkin;

import java.util.Map;

/**
 * Класс, представляющий операцию вычитания двух выражений.
 */
public class Sub extends Expression {
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
    public int normalizedEval(Map<String, Integer> vars) {
        return left.normalizedEval(vars) - right.normalizedEval(vars);
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
     * Вывод выражения вычитания.
     */
    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }
}