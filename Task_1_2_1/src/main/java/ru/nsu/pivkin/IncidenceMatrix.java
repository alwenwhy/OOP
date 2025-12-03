package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Реализация интерфейса Graph на основе матрицы инцидентности.
 * Матрица инцидентности хранится как список столбцов, где каждый столбец
 * представляет одно ребро, показывающее с какими вершинами оно инцидентно.
 */
public class IncidenceMatrix implements Graph {
    private final boolean directed;
    private final List<Integer> vertices = new ArrayList<>();
    private final List<int[]> edges = new ArrayList<>();

    /**
     * Создаёт пустой граф на основе матрицы инцидентности.
     *
     * @param directed - ориентированный ли граф.
     */
    public IncidenceMatrix(boolean directed) {
        this.directed = directed;
    }

    /**
     * Добавляет вершину в граф. При добавлении вершины существующие
     * столбцы (рёбра) расширяются на одну строку.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно добавлена, иначе false.
     */
    @Override
    public boolean addVertex(int vertex) {
        if (vertex < 0 || vertices.contains(vertex)) {
            return false;
        }

        vertices.add(vertex);

        for (int i = 0; i < edges.size(); i++) {
            int[] old = edges.get(i);
            int[] expanded = Arrays.copyOf(old, vertices.size());
            edges.set(i, expanded);
        }

        return true;
    }

    /**
     * Удаляет вершину и соответствующую строку в каждом столбце.
     * Если вершина участвовала в ребре, то соответствующее ребро полностью удаляется.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно удалена, иначе false.
     */
    @Override
    public boolean removeVertex(int vertex) {
        int idx = indexOf(vertex);
        if (idx == -1) {
            return false;
        }

        vertices.remove(idx);

        for (int i = 0; i < edges.size(); ) {
            int[] old = edges.get(i);

            if (old[idx] != 0) {
                edges.remove(i);
                continue;
            }

            int[] updated = new int[old.length - 1];
            for (int r = 0, w = 0; r < old.length; r++) {
                if (r != idx) {
                    updated[w++] = old[r];
                }
            }
            edges.set(i, updated);
            i++;
        }

        return true;
    }

    /**
     * Добавляет ребро между вершинами.
     * Добавляет новый столбец (ребро) в матрицу инцидентности.
     * Вершины автоматически добавляются, если их не было ранее.
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

        int size = vertices.size();
        int[] col = new int[size]; // новый столбец

        int iFrom = indexOf(from);
        int iTo = indexOf(to);

        if (directed) {
            col[iFrom] = -1;
            col[iTo] = 1;
        } else {
            col[iFrom] = 1;
            col[iTo] = 1;
        }

        edges.add(col);
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
        int iFrom = indexOf(from);
        int iTo = indexOf(to);

        if (iFrom == -1 || iTo == -1) {
            return false;
        }

        for (int i = 0; i < edges.size(); i++) {
            int[] col = edges.get(i);

            if (directed) {
                if (col[iFrom] == -1 && col[iTo] == 1) {
                    edges.remove(i);
                    return true;
                }
            } else {
                if (col[iFrom] == 1 && col[iTo] == 1) {
                    edges.remove(i);
                    return true;
                }
            }
        }

        return false;
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
        List<Integer> nb = new ArrayList<>();

        int idx = indexOf(vertex);
        if (idx == -1) {
            return nb;
        }

        for (int[] e : edges) {
            if (e[idx] != 0) {
                for (int i = 0; i < vertices.size(); i++) {
                    if (i != idx && e[i] > 0) {
                        nb.add(vertices.get(i));
                    }
                }
            }
        }

        return nb;
    }

    /**
     * Проверяет, является ли граф ориентированным.
     *
     * @return - true если граф ориентированный, иначе false.
     */
    @Override
    public boolean isDirected() {
        return this.directed;
    }

    /**
     * Возвращает строковое представление графа:
     * список вершин и матрицу смежности, содержащую только используемые индексы.
     * Пример:
     * Incidence Matrix Graph (directed)
     * Vertices: [2, 1, 4, 3]
     * Matrix:
     * \ e0 e1 e2
     * 2 -1  0  0
     * 1  1  1  0
     * 4  0 -1  1
     * 3  0  0 -1
     *
     * @return - строковое описание графа.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Incidence Matrix Graph (")
                .append(directed ? "directed" : "undirected")
                .append(")\nVertices: ")
                .append(vertices)
                .append("\nMatrix:\n\\ ");

        for (int i = 0; i < edges.size(); i++) {
            sb.append("e").append(i).append(" ");
        }
        sb.append("\n");

        for (int v : vertices) {
            sb.append(v).append(" ");

            int idx = indexOf(v);
            for (int[] col : edges) {
                if(col[idx] >= 0) {
                    sb.append(" ");
                }
                sb.append(col[idx]).append(" ");
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

    /**
     * Возвращает индекс вершины в списке vertices.
     *
     * @param vertex - вершина
     * @return - индекс вершины или -1 если она отсутствует.
     */
    private int indexOf(int vertex) {
        return vertices.indexOf(vertex);
    }
}
