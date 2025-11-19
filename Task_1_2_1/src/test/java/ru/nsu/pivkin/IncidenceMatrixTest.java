package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IncidenceMatrixTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph g = new IncidenceMatrix(true);
        assertTrue(g.addVertex(1));
        assertTrue(g.addVertex(2));
        assertFalse(g.addVertex(-1)); // отрицательная вершина
        assertFalse(g.addVertex(1));  // дубликат вершины

        List<Integer> vertices = g.getVertices();
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));

        assertTrue(g.removeVertex(1));
        assertFalse(g.getVertices().contains(1));
        assertFalse(g.removeVertex(10)); // несуществующая вершина
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph g = new IncidenceMatrix(true);
        g.addVertex(1);
        g.addVertex(2);

        assertTrue(g.addEdge(1, 2));
        List<Integer> neighbors = g.getNeighbors(1);
        assertEquals(1, neighbors.size());
        assertEquals(2, neighbors.get(0));

        assertTrue(g.removeEdge(1, 2));
        assertTrue(g.getNeighbors(1).isEmpty());

        // Удаляем несуществующее ребро
        assertFalse(g.removeEdge(2, 3));

        // Неориентированный граф
        Graph undirected = new IncidenceMatrix(false);
        undirected.addEdge(3, 4);
        assertTrue(undirected.getNeighbors(3).contains(4));
        assertTrue(undirected.getNeighbors(4).contains(3));
    }

    @Test
    void testAutoAddVertexOnEdge() {
        Graph g = new IncidenceMatrix(true);

        // Вершины 1 и 2 автоматически добавляются при добавлении ребра
        assertTrue(g.addEdge(1, 2));
        assertTrue(g.getVertices().contains(1));
        assertTrue(g.getVertices().contains(2));

        // Проверка соседей
        List<Integer> neighbors1 = g.getNeighbors(1);
        assertEquals(1, neighbors1.size());
        assertEquals(2, neighbors1.get(0));
    }

    @Test
    void testRemoveVertexRemovesEdges() {
        Graph g = new IncidenceMatrix(true);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        // Удаляем вершину 2
        assertTrue(g.removeVertex(2));
        assertFalse(g.getVertices().contains(2));
        assertTrue(g.getNeighbors(1).isEmpty()); // ребро 1->2 удалилось
        assertTrue(g.getNeighbors(3).isEmpty()); // ребро 2->3 удалилось
    }

    @Test
    void testGetNeighbors() {
        Graph g = new IncidenceMatrix(true);
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
        Graph g = new IncidenceMatrix(true);
        assertTrue(g.isDirected());

        Graph undirected = new IncidenceMatrix(false);
        assertFalse(undirected.isDirected());
    }

    @Test
    void testToString() {
        Graph g = new IncidenceMatrix(true);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        String str = g.toString();
        assertTrue(str.contains("Incidence Matrix Graph (directed)"));
        assertTrue(str.contains("Vertices:"));
        assertTrue(str.contains("1 "));
        assertTrue(str.contains("2 "));
        assertTrue(str.contains("3 "));
    }

    @Test
    void testIsomorphic() {
        Graph g1 = new IncidenceMatrix(true);
        g1.addEdge(1, 2);
        g1.addEdge(2, 3);

        Graph g2 = new IncidenceMatrix(true);
        g2.addEdge(4, 5);
        g2.addEdge(5, 6);

        assertTrue(g1.equals(g2));
    }
}


