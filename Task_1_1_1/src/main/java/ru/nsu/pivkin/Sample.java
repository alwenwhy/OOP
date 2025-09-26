package ru.nsu.pivkin;

public class Sample {
    private static void heapify(int[] input, int n, int i){
        int largest = i;

        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if(left < n && input[left] > input[largest])
            largest = left;

        if(right < n && input[right] > input[largest])
            largest = right;

        if(largest != i){
            //swap
            int buffer = input[i];
            input[i] = input[largest];
            input[largest] = buffer;

            heapify(input, n, largest);
        }
    }

    public static void heapSort(int[] input){
        int n = input.length;

        for(int i = n / 2 - 1; i >= 0; i--)
            heapify(input, n, i);

        for(int i = n - 1; i >= 0; i--){
            int buffer = input[0];
            input[0] = input[i];
            input[i] = buffer;

            heapify(input, i, 0);
        }
    }


    public static void main(String[] args){
        int arr[] = {12, 11, 13, 5, 6, 7};
        int n = arr.length;

        System.out.print("Source array: ");
        for(int i = 0; i < n; i++)
            System.out.print(arr[i] + " ");
        System.out.println();

        Sample ob = new Sample();
        ob.heapSort(arr);

        System.out.print("Sorted array: ");
        for(int i = 0; i < n; i++)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}
