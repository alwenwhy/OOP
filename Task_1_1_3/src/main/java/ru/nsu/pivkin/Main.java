package ru.nsu.pivkin;

/**
 * Пример использования классов выражений.
 * Демонстрирует создание, печать, дифференцирование и вычисление выражения.
 */
public class Main {
    public static void main(String[] args) {
        // (3 + (2 * x))
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));

        System.out.print("Expression e = ");
        e.print();
        System.out.println();

        System.out.print("Derivative de/dx = ");
        Expression de = e.derivative("x");
        de.print();
        System.out.println();

        int result = e.eval("x=10; y=13");
        System.out.println("Eval for x=10; y=13 -> " + result);
    }
}
