package ru.nsu.pivkin;

/**
 * Класс для демонстрации работоспособности.
 */
public class Main {
    /**
     * Пример использования классов выражений.
     * Демонстрирует создание, печать, дифференцирование и вычисление выражения.
     */

    public static void main(String[] args) {
        // (3 + (2 * x))
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));

        System.out.print("Expression e = ");
        System.out.println(e);

        System.out.print("Derivative de/dx = ");
        Expression de = e.derivative("x");
        System.out.println(de);

        int result = ExpressionUtil.evaluate(e, "x=10; y=13");
        System.out.println("Eval for x=10; y=13 -> " + result);
    }
}
