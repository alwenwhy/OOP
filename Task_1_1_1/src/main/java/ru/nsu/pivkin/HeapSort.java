package ru.nsu.pivkin;

/**
 * Класс для сортировки массива методом пирамидальной сортировки (Heap Sort).
 * Работает в худшем, среднем и лучшем случае за O(n log n).
 */
public class HeapSort {
    /**
     * Функция преобразования в двоичную кучу max-heap поддерева с корнем в заданном индексе.
     *
     * @param input массив, представляющий кучу
     * @param n размер кучи
     * @param i индекс корня поддерева
     */
    private static void heapify(int[] input, int n, int i) {
        int largest = i;

        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && input[left] > input[largest]) {
            largest = left;
        }
        if (right < n && input[right] > input[largest]) {
            largest = right;
        }

        if (largest != i) {
            //swap
            int buffer = input[i];
            input[i] = input[largest];
            input[largest] = buffer;

            heapify(input, n, largest);
        }
    }

    /**
     * Сортировка массива методом пирамидальной сортировки.
     * Этапы:
     * 1. Построение двоичной кучи max-heap из исходного массива;
     * 2. Последовательное извлечение максимальных элементов
     *    с восстановлением свойств на меньшей куче.
     *
     * @param input массив для сортировки (результат сохраняется тут же)
     */
    public static void heapSort(int[] input) {
        int n = input.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(input, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int buffer = input[0];
            input[0] = input[i];
            input[i] = buffer;

            heapify(input, i, 0);
        }
    }

    /**
     * Демонстрационный вариант работы алгоритма.
     * 
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        int arr[] = {12, 11, 13, 5, 6, 7};
        int n = arr.length;

        System.out.print("Source array: ");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        HeapSort ob = new HeapSort();
        ob.heapSort(arr);

        System.out.print("Sorted array: ");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
