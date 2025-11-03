package ru.nsu.pivkin;

import java.util.HashMap;
import java.util.Map;

public final class ExpressionUtil {
    /**
     * Вычисление значения выражения при заданом наборе переменных.
     * Переменные заданы в виде строки "x=1;y=2" ...
     */

    public static int evaluate(Expression expression, String variables) {
         return expression.eval(parser(variables));
    }

    /**
     * Парсер строки в структуру данных Map.
     *
     * @param stringVars - строка переменных типа "имя=значение", разделённых ";"
     * @return - Map (пары вида "ключ-значение")
     */
    private static Map<String, Integer> parser(String stringVars) {
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

    private ExpressionUtil() {
        throw new UnsupportedOperationException();
    }
}
