package ru.nsu.pivkin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    void testSortEmptyArray() {
        int[] input = {};
        int[] expected = {};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSortSingleElement() {
        int[] input = {5};
        int[] expected = {5};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSortSimpleArray() {
        int[] input = {3, 1, 2};
        int[] expected = {1, 2, 3};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSortAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSortNegativeNumbers() {
        int[] input = {-3, 5, -1, 0, -7, 2, 8, -4};
        int[] expected = {-7, -4, -3, -1, 0, 2, 5, 8};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSortBigValues() {
        int[] input = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};

        HeapSort.heapSort(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void checkMain() {
        HeapSort.main(new String[] {});
        assertTrue(true);
    }
}
