package ru.nsu.pivkin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тест основной программы.
 * И топологической сортировки.
 */
public class MainTest {
    @Test
    void checkMain() {
        Main.main(new String[] {"test.txt"});
        assertTrue(true);
    }

    @Test
    void checkIsomorphismDiffDirections() {
        Graph g1 = new AdjacencyMatrix(true);
        Graph g2 = new AdjacencyMatrix(false);

        g1.addEdge(1, 2);
        g2.addEdge(1, 2);

        assertFalse(g1.equals(g2));
        assertFalse(g2.equals(g1));
    }

    @Test
    void checkIsomorphismDiffSizes() {
        Graph g1 = new AdjacencyMatrix(true);
        Graph g2 = new AdjacencyMatrix(true);

        g1.addVertex(1);
        g1.addVertex(2);

        g2.addVertex(1);

        assertFalse(g1.equals(g2));
        assertFalse(g2.equals(g1));
    }

    @Test
    void checkIsomorphism() {
        Graph g1 = new AdjacencyMatrix(false);
        g1.addEdge(1, 4);
        g1.addEdge(1, 5);
        g1.addEdge(1, 6);
        g1.addEdge(2, 4);
        g1.addEdge(2, 6);
        g1.addEdge(3, 4);
        g1.addEdge(3, 5);
        g1.addEdge(3, 6);

        Graph g2 = new AdjacencyList(false);
        g2.addEdge(1, 5);
        g2.addEdge(1, 4);
        g2.addEdge(1, 6);
        g2.addEdge(4, 3);
        g2.addEdge(4, 2);
        g2.addEdge(2, 5);
        g2.addEdge(2, 6);
        g2.addEdge(6, 3);

        assertTrue(g1.equals(g2));
        assertTrue(g2.equals(g1));
    }
}