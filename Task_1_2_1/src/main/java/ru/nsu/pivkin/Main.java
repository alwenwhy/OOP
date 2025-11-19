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
            throw new RuntimeException(e);
        }

        System.out.println(g);
        System.out.println(TopologicalSort.sort(g));

        g.addEdge(4, 3);
        g.addEdge(2, 3);

        System.out.println(g);

        g.removeVertex(2);

        System.out.println(g);
        System.out.println(g.equals(g));
    }
}
