package ru.nsu.pivkin;

import java.io.IOException;

/**
 * Класс для демонстрации работоспособности.
 */
public class Main {
    /**
     * Пример использование графов.
     *
     * @param args - не используется.
     */
    public static void main(String[] args) {
        Graph g = new AdjacencyMatrix(true);

        try {
            GraphUtil.loadFromFile(args[0], g);
        } catch (IOException e) {
            g.addEdge(2, 1);
            g.addEdge(4, 2);
            g.addEdge(3, 1);
        }

        System.out.println(g);
        System.out.println(TopologicalSort.sort(g));
    }
}
