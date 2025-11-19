package ru.nsu.pivkin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Класс с алгоритмом, реализующий топологическую сортировку для ориентированных графов.
 */
public class TopologicalSort {
    /**
     * Топологическая сортировка алгоритмом Кана.
     *
     * @param graph - граф, который требуется отсортировать топологически.
     * @return - список вершин в топологически отсортированном порядке.
     *
     * @throws IllegalArgumentException - если граф не ориентированный.
     * @throws IllegalStateException - если граф содержит хотя бы один цикл.
     */
    public static List<Integer> sort(Graph graph) {
        if (!graph.isDirected()) {
            throw new IllegalArgumentException("Граф неориентированный.");
        }

        List<Integer> vertices = graph.getVertices();
        Map<Integer, Integer> indegree = new HashMap<>();

        for (int v : vertices) {
            indegree.put(v, 0);
        }

        for (int u : vertices) {
            for (int neighbor : graph.getNeighbors(u)) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int v : vertices) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        List<Integer> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);

            for (int neighbor : graph.getNeighbors(u)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalStateException("Граф содержит цикл, сортировка невозможна.");
        }

        return result;
    }
}



