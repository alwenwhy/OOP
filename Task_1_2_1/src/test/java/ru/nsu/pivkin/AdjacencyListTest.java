package ru.nsu.pivkin;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса AdjacencyList.
 */
class AdjacencyListTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph g = new AdjacencyList(true);

        assertTrue(g.addVertex(1));
        assertTrue(g.addVertex(2));

        assertFalse(g.addVertex(-1));

        assertTrue(g.getVertices().contains(1));
        assertTrue(g.getVertices().contains(2));

        assertTrue(g.removeVertex(1));

        assertFalse(g.getVertices().contains(1));
        assertFalse(g.removeVertex(10));
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph g = new AdjacencyList(true);
        g.addVertex(1);
        g.addVertex(2);

        assertTrue(g.addEdge(1, 2));
        List<Integer> neighbors = g.getNeighbors(1);
        assertEquals(1, neighbors.size());
        assertEquals(2, neighbors.get(0));

        assertTrue(g.removeEdge(1, 2));
        assertTrue(g.getNeighbors(1).isEmpty());

        // Неориентированный граф
        Graph undirected = new AdjacencyList(false);
        undirected.addEdge(3, 4);
        assertTrue(undirected.getNeighbors(3).contains(4));
        assertTrue(undirected.getNeighbors(4).contains(3));
    }

    @Test
    void testGetNeighbors() {
        Graph g = new AdjacencyList(true);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        List<Integer> neighbors1 = g.getNeighbors(1);
        assertEquals(2, neighbors1.size());
        assertTrue(neighbors1.contains(2));
        assertTrue(neighbors1.contains(3));

        List<Integer> neighbors3 = g.getNeighbors(3);
        assertTrue(neighbors3.isEmpty());
    }

    @Test
    void testIsDirected() {
        Graph g = new AdjacencyList(true);
        assertTrue(g.isDirected());

        Graph undirected = new AdjacencyList(false);
        assertFalse(undirected.isDirected());
    }

    @Test
    void testToString() {
        Graph g = new AdjacencyList(true);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        String str = g.toString();
        assertTrue(str.contains("Adjacency List Graph (directed)"));
        assertTrue(str.contains("1 -> [2]"));
        assertTrue(str.contains("2 -> [3]"));
    }

    @Test
    void testIsomorphic() {
        Graph g1 = new AdjacencyList(true);
        g1.addEdge(1, 2);
        g1.addEdge(2, 3);

        Graph g2 = new AdjacencyList(true);
        g2.addEdge(4, 5);
        g2.addEdge(5, 6);

        assertTrue(g1.equals(g2));
    }

    @Test
    void testHashCodeForIsomorphicGraphs() {
        Graph g1 = new AdjacencyList(true);
        g1.addEdge(1, 2);
        g1.addEdge(2, 3);

        Graph g2 = new AdjacencyList(true);
        g2.addEdge(4, 5);
        g2.addEdge(5, 6);

        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void testHashCodeForNonIsomorphicGraphs() {
        Graph g1 = new AdjacencyList(true);
        g1.addEdge(1, 2);

        Graph g2 = new AdjacencyList(true);
        g2.addEdge(1, 2);
        g2.addEdge(2, 3);

        assertNotEquals(g1.hashCode(), g2.hashCode());
    }
}

