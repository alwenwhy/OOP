package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса Graph на основе матрицы смежности.
 * Граф хранится в виде квадратной матрицы, где matrix[u][v] == 1
 * если между вершинами (u, v) есть ребро.
 */
public class AdjacencyMatrix implements Graph {
    private static final int INIT_CAPACITY = 4;
    private int[][] matrix;
    private final Map<Integer, Integer> indexMap = new HashMap<>();
    private final List<Integer> vertices = new ArrayList<>();
    private final boolean directed;

    /**
     * Создаёт пустой граф на основе матрицы смежности.
     *
     * @param directed - ориентированный ли граф.
     */
    public AdjacencyMatrix(boolean directed) {
        this.directed = directed;
        this.matrix = new int[INIT_CAPACITY][INIT_CAPACITY];
    }

    /**
     * Добавляет вершину в граф.
     * Увеличивает матрицу, если нет места.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно добавлена, иначе false.
     */
    @Override
    public boolean addVertex(int vertex) {
        if (vertex < 0 || indexMap.containsKey(vertex)) {
            return false;
        }

        int idx = vertices.size();
        vertices.add(vertex);
        indexMap.put(vertex, idx);

        if (idx >= matrix.length) {
            int old = matrix.length;
            int now = old * 2;

            int[][] newM = new int[now][now];
            for (int i = 0; i < old; i++) {
                System.arraycopy(matrix[i], 0, newM[i], 0, old);
            }
            matrix = newM;
        }

        return true;
    }

    /**
     * Удаляет вершину из графа вместе со всеми инцидентными ей рёбрами.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно удалена, иначе false.
     */
    @Override
    public boolean removeVertex(int vertex) {
        Integer idxObj = indexMap.get(vertex);
        if (idxObj == null) {
            return false;
        }

        int idx = idxObj;

        // удалить из списков
        vertices.remove(idx);
        indexMap.remove(vertex);

        // сдвинуть строки и столбцы
        int n = vertices.size() + 1;

        for (int i = idx; i < n - 1; i++) {
            matrix[i] = matrix[i + 1];
        }
        for (int i = 0; i < n - 1; i++) {
            System.arraycopy(matrix[i], idx + 1, matrix[i], idx, n - 1 - idx);
        }

        // переиндексировать вершины
        indexMap.clear();
        for (int i = 0; i < vertices.size(); i++) {
            indexMap.put(vertices.get(i), i);
        }

        return true;
    }

    /**
     * Добавляет ребро между вершинами.
     * В ориентированных графах ребро идёт от from до to.
     * В неориентированных графах ребро идёт в обе стороны.
     * Если вершин не существуют - они автоматически добавляются (если можно).
     *
     * @param from - начальная вершина.
     * @param to - конечная вершина.
     * @return - true, если ребро успешно добавлено, иначе false.
     */
    @Override
    public boolean addEdge(int from, int to) {
        if (from < 0 || to < 0) {
            return false;
        }

        addVertex(from);
        addVertex(to);

        int u = indexMap.get(from);
        int v = indexMap.get(to);

        matrix[u][v] = 1;
        if (!directed) {
            matrix[v][u] = 1;
        }

        return true;
    }

    /**
     * Удаляет ребро между вершинами.
     *
     * @param from - начальная вершина.
     * @param to - конечная вершина.
     * @return - true, если ребро успешно удалено, иначе false.
     */
    @Override
    public boolean removeEdge(int from, int to) {
        if (!indexMap.containsKey(from) || !indexMap.containsKey(to)) {
            return false;
        }

        int u = indexMap.get(from);
        int v = indexMap.get(to);

        matrix[u][v] = 0;
        if (!directed) {
            matrix[v][u] = 0;
        }

        return true;
    }

    /**
     * Возвращает список всех вершин графа.
     *
     * @return - список меток всех вершин графа.
     */
    @Override
    public List<Integer> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    /**
     * Возвращает список всех соседних вершин для указанной вершины.
     * В ориентированном графе в списке только те вершины, в которые можно
     * попасть из заданной.
     *
     * @param vertex - вершина, для которой нужно получить соседей.
     * @return - список соседей вершины, может быть пустым.
     */
    @Override
    public List<Integer> getNeighbors(int vertex) {
        Integer idx = indexMap.get(vertex);
        if (idx == null) {
            return List.of();
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            if (matrix[idx][i] == 1) {
                res.add(vertices.get(i));
            }
        }
        return res;
    }

    /**
     * Проверяет, является ли граф ориентированным.
     *
     * @return - true если граф ориентированный, иначе false.
     */
    @Override
    public boolean isDirected() {
        return directed;
    }

    /**
     * Возвращает строковое представление графа:
     * список вершин и матрицу смежности, содержащую только используемые индексы.
     *
     * @return - строковое описание графа.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix Graph (")
                .append(directed ? "directed" : "undirected")
                .append(")\nVertices: ")
                .append(vertices)
                .append("\nMatrix:\n\\ ");

        for (int v : vertices) {
            sb.append(v).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < vertices.size(); i++) {
            sb.append(vertices.get(i)).append(" ");
            for (int j = 0; j < vertices.size(); j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Проверяет равенство двух графов (изоморфизм).
     *
     * @param obj - объект, с которым сравнивается граф.
     * @return - true если графы изоморфны, иначе false.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Graph other)) {
            return false;
        }
        return GraphUtil.isIsomorphic(this, other);
    }

    /**
     * Возвращает хеш-код графа.
     *
     * @return - хеш-код, основанный на отсортированном списке степеней вершин графа.
     */
    @Override
    public int hashCode() {
        List<Integer> vertices = getVertices();
        List<Integer> degrees = new ArrayList<>();

        for (int v : vertices) {
            degrees.add(getNeighbors(v).size());
        }

        Collections.sort(degrees);
        return degrees.hashCode();
    }
}

