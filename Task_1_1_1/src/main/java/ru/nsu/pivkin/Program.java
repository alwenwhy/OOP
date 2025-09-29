package ru.nsu.pivkin;

/**
 * Демонстрационный класс для работы алгоритма пирамидальной сортировки.
 */
public class Program {
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

        HeapSort.heapSort(arr);

        System.out.print("Sorted array: ");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}