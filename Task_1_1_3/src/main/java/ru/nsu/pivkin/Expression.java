package ru.nsu.pivkin;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный класс для всех математических выражений.
 * Разбивает строку на переменные и определяет методы для
 * вычисления, дифференцирования и вывода.
 */
public abstract class Expression {
    public abstract int normalizedEval(Map<String, Integer> vars);
    public abstract Expression derivative(String variable);
    public abstract void print();

    /**
     * Вычисление значения выражения при заданной строке переменных.
     *
     * @param stringVars - строка переменных типа "имя=значение", разделённых ";"
     * @return - результат вычисления выражения
     */
    public int eval(String stringVars) {
        Map<String, Integer> vars = this.parser(stringVars);
        return normalizedEval(vars);
    }

    /**
     * Парсер строки в структуру данных Map.
     *
     * @param stringVars - строка переменных типа "имя=значение", разделённых ";"
     * @return - Map (пары вида "ключ-значение")
     */
    public static Map<String, Integer> parser(String stringVars) {
        Map<String, Integer> map = new HashMap<>();
        if (stringVars == null || stringVars.isBlank()) {
            return map;
        }

        String[] parts = stringVars.split(";");
        int n = parts.length;
        for (int i = 0; i < n; i++) {
            String trimmed = parts[i].trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            String[] kv = trimmed.split("="); //key-var part
            if (kv.length == 2) {
                String key = kv[0].trim();
                int var = Integer.parseInt(kv[1].trim());

                map.put(key, var);
            }
        }

        return map;
    }
}
