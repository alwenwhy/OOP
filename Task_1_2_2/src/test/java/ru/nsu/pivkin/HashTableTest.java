package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки класса HashTable.
 */
class HashTableTest {
    @Test
    void testMain() {
        Program.main(new String[] {});
        assertTrue(true);
    }

    @Test
    void testPutAndGet() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.put("one", 1));
        assertEquals(1, table.get("one"));
        assertNull(table.get("two"));

        assertEquals(1, table.put("one", 10));
        assertEquals(10, table.get("one"));
    }

    @Test
    void testRemove() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);

        assertEquals(1, table.remove("a"));
        assertNull(table.get("a"));
        assertEquals(2, table.get("b"));
        assertNull(table.remove("c"));
    }

    @Test
    void testUpdate() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("x", 5);

        table.update("x", 50);
        assertEquals(50, table.get("x"));

        assertThrows(NoSuchElementException.class, () -> table.update("y", 100));
    }

    @Test
    void testContainsKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 123);

        assertTrue(table.containsKey("key"));
        assertFalse(table.containsKey("other"));
    }

    @Test
    void testToString() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        String str = table.toString();
        assertTrue(str.contains("one=1"));
        assertTrue(str.contains("two=2"));
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> table1 = new HashTable<>();
        HashTable<String, Integer> table2 = new HashTable<>();

        assertEquals(table1, table2);

        table1.put("a", 1);
        table1.put("b", 2);

        table2.put("b", 2);
        table2.put("a", 1);

        assertEquals(table1, table2);

        table2.put("c", 3);
        assertNotEquals(table1, table2);
    }

    @Test
    void testResizedTable() {
        HashTable<Integer, String> table = new HashTable<>();
        int initialCapacity = 10;

        for (int i = 0; i < 20; i++) {
            table.put(i, "val" + i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals("val" + i, table.get(i));
        }
    }

    @Test
    void testIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);

        int sum = 0;
        for (Map.Entry<String, Integer> entry : table) {
            sum += entry.getValue();
        }

        assertEquals(3, sum);
    }
}

