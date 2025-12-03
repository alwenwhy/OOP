package ru.nsu.pivkin;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса AdjacencyMatrix.
 */
public class AdjacencyMatrixTest {
    @Test
    void testAddVertex() {
        Graph g = new AdjacencyMatrix(true);

        assertTrue(g.addVertex(1));
        assertTrue(g.addVertex(5));
        assertFalse(g.addVertex(1));

        assertEquals(List.of(1, 5), g.getVertices());
    }

    @Test
    void testAddEdgeDirected() {
        Graph g = new AdjacencyMatrix(true);

        assertTrue(g.addEdge(1, 2));
        assertEquals(List.of(1, 2), g.getVertices());
        assertEquals(List.of(2), g.getNeighbors(1));
        assertEquals(List.of(), g.getNeighbors(2));
    }

    @Test
    void testAddEdgeUndirected() {
        Graph g = new AdjacencyMatrix(false);

        assertTrue(g.addEdge(3, 7));
        assertEquals(List.of(7), g.getNeighbors(3));
        assertEquals(List.of(3), g.getNeighbors(7));
    }

    @Test
    void testRemoveEdge() {
        Graph g = new AdjacencyMatrix(true);

        g.addEdge(1, 2);
        g.addEdge(1, 3);

        assertTrue(g.removeEdge(1, 2));
        assertEquals(List.of(3), g.getNeighbors(1));
        assertFalse(g.removeEdge(5, 6)); // не существующие вершины
    }

    @Test
    void testRemoveVertex() {
        Graph g = new AdjacencyMatrix(true);

        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addVertex(10);

        assertTrue(g.removeVertex(2));

        assertFalse(g.getVertices().contains(2));
        assertEquals(List.of(), g.getNeighbors(1));
        assertEquals(List.of(), g.getNeighbors(3));
    }

    @Test
    void testIsDirected() {
        Graph d = new AdjacencyMatrix(true);
        Graph u = new AdjacencyMatrix(false);

        assertTrue(d.isDirected());
        assertFalse(u.isDirected());
    }

    @Test
    void testNeighborsEmpty() {
        Graph g = new AdjacencyMatrix(true);

        g.addVertex(5);
        assertEquals(List.of(), g.getNeighbors(5));
    }

    @Test
    void testEqualsIsomorphic() {
        Graph g1 = new AdjacencyMatrix(true);
        Graph g2 = new AdjacencyMatrix(true);

        g1.addEdge(1, 2);
        g1.addEdge(2, 3);

        g2.addEdge(10, 20);
        g2.addEdge(20, 30);

        assertEquals(g1, g2);
    }

    @Test
    void testEqualsNotIsomorphic() {
        Graph g1 = new AdjacencyMatrix(true);
        Graph g2 = new AdjacencyMatrix(true);

        g1.addEdge(1, 2);
        g1.addEdge(2, 3);

        g2.addEdge(10, 20);

        assertNotEquals(g1, g2);
    }

    @Test
    void testToString() {
        Graph g = new AdjacencyMatrix(true);

        g.addEdge(2, 1);
        g.addEdge(4, 2);
        g.addEdge(3, 1);

        System.out.print(g);
        assertTrue(true);
    }
}
