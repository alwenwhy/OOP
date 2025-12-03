package ru.nsu.pivkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Реализация интерфейса Graph на основе списка смежности.
 * Каждая вершина отображается в множество соседей.
 */
public class AdjacencyList implements Graph {
    private final Map<Integer, Set<Integer>> adj = new HashMap<>();
    private final boolean directed;

    /**
     * Создаёт пустой граф на основе списка смежности.
     *
     * @param directed - ориентированный ли граф.
     */
    public AdjacencyList(boolean directed) {
        this.directed = directed;
    }

    /**
     * Добавляет вершину в граф. Если вершина уже существует,
     * метод не изменяет структуру, но возвращает true.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно добавлена, иначе false.
     */
    @Override
    public boolean addVertex(int vertex) {
        if (vertex < 0) {
            return false;
        }

        adj.putIfAbsent(vertex, new HashSet<>());
        return true;
    }

    /**
     * Удаляет вершину и удаляет её из списков соседей.
     *
     * @param vertex - вершина, целое неотрицательное число.
     * @return - true, если вершина успешно удалена, иначе false.
     */
    @Override
    public boolean removeVertex(int vertex) {
        if (!adj.containsKey(vertex)) {
            return false;
        }

        adj.remove(vertex);

        for (Set<Integer> s : adj.values()) {
            s.remove(vertex);
        }

        return true;
    }

    /**
     * Добавляет ребро между вершинами.
     * В ориентированных графах ребро идёт от from до to.
     * В неориентированных графах ребро идёт в обе стороны.
     * Если вершин не существуют - они автоматически добавяются (если можно).
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

        adj.get(from).add(to);
        if (!directed) {
            adj.get(to).add(from);
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
        if (!adj.containsKey(from)) {
            return false;
        }

        adj.get(from).remove(to);
        if (!directed && adj.containsKey(to)) {
            adj.get(to).remove(from);
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
        return List.copyOf(adj.keySet());
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
        return new ArrayList<>(adj.getOrDefault(vertex, Set.of()));
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
     * Adjacency List Graph (directed)
     * Vertices: [1, 2, 3, 4]
     * Adjacency List:
     * \ Neighbors
     * 1 -> []
     * 2 -> [1]
     * 3 -> [4]
     * 4 -> [1]
     *
     * @return - строковое описание графа.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency List Graph (")
                .append(directed ? "directed" : "undirected")
                .append(")\nVertices: ")
                .append(adj.keySet())
                .append("\nAdjacency List:\n\\ Neighbors\n");

        List<Integer> sorted = new ArrayList<>(adj.keySet());
        Collections.sort(sorted);

        for (int v : sorted) {
            sb.append(v).append(" -> ");

            List<Integer> neighbors = new ArrayList<>(adj.get(v));
            Collections.sort(neighbors);

            sb.append(neighbors).append("\n");
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
