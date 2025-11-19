package ru.nsu.pivkin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилитный класс для работы с графами.
 */
public class GraphUtil {
    /**
     * Загружает граф из файла.
     * Формат входного файла:
     * V E
     * u1 v1
     * u2 v2
     * ...
     * uE vE
     * где V - кол-во рёбер.
     *     E - кол-во вершин.
     *     ui vi - ребро между указанными вершинами.
     *
     * @param path - путь к файлу.
     * @param g - объект графа.
     *
     * @throws IOException - если файл не удаётся открыть, прочитать или формат файла некорректен.
     *                       В частности, выбрасывается, если файл закончился раньше времени или
     *                       строка с ребром имеет неверный формат.
     */
    public static void loadFromFile(String path, Graph g) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] parts = br.readLine().trim().split("\\s+");
            int V = Integer.parseInt(parts[0]);
            int E = Integer.parseInt(parts[1]);

            for (int i = 0; i < E; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Внезапный конец файла");
                }

                String[] uv = line.trim().split("\\s+");
                int u = Integer.parseInt(uv[0]);
                int v = Integer.parseInt(uv[1]);

                g.addEdge(u, v);
            }
        }
    }

    /**
     * Проверка двух графов на изоморфизм.
     *
     * @param g1 - первый граф
     * @param g2 - второй граф
     * @return - true, если графы изоморфны, иначе false.
     */
    public static boolean isIsomorphic(Graph g1, Graph g2) {
        List<Integer> list1 = g1.getVertices();
        List<Integer> list2 = g2.getVertices();

        if (g1.isDirected() != g2.isDirected()) {
            return false;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        int n = list1.size();
        int[] perm = new int[n];

        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }

        do {
            if (checkMapping(g1, g2, list1, list2, perm)) {
                return true;
            }
        } while (nextPermutation(perm));

        return false;
    }

    /**
     * Проверяет, сохраняет ли заданная перестановка вершин
     * из L1 -> L2 структуру рёбер графа.
     *
     * @param g1 - первый граф
     * @param g2 - второй граф
     * @param L1 - список вершин первого графа
     * @param L2 - список вершин второго графа
     * @param perm - перестановка индексов L2, задающая отображение вершин
     * @return - true если отображение изоморфно, иначе false.
     */
    private static boolean checkMapping(Graph g1, Graph g2,
                                        List<Integer> L1, List<Integer> L2,
                                        int[] perm) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < L1.size(); i++) {
            map.put(L1.get(i), L2.get(perm[i]));
        }

        for (int u : L1) {
            for (int v : g1.getNeighbors(u)) {
                int u2 = map.get(u);
                int v2 = map.get(v);

                if (!g2.getNeighbors(u2).contains(v2)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Генерирует следующую лексикографическую перестановку массива a.
     *
     * @param a  - массив перестановки
     * @return true - если была создана следующая перестановка.
     *         false - если текущая перестановка - последняя (по убыванию).
     */
    private static boolean nextPermutation(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i] > a[i + 1]) {
            i--;
        }

        if (i < 0) {
            return false;
        }

        int j = a.length - 1;
        while (a[j] < a[i]) {
            j--;
        }

        int t = a[i];
        a[i] = a[j];
        a[j] = t;

        for (int l = i + 1, r = a.length - 1; l < r; l++, r--) {
            t = a[l];
            a[l] = a[r];
            a[r] = t;
        }

        return true;
    }
}
