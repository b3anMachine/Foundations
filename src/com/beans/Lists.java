package com.beans;

import java.util.Arrays;

public class Lists {
    public static void main(String... args) {
        ListsMethods ls = new ListsPractice();

        System.out.println("Insertion sort: ");
        System.out.println(isSorted(ls.insertionSort(getTestArray(0))));
        System.out.println(isSorted(ls.insertionSort(getTestArray(1))));
        System.out.println(isSorted(ls.insertionSort(getTestArray(2))));
        System.out.println(isSorted(ls.insertionSort(getTestArray(10))));
        System.out.println(isSorted(ls.insertionSort(getTestArray(100))));
        System.out.println();
        System.out.println("Selection sort: ");
        System.out.println(isSorted(ls.selectionSort(getTestArray(0))));
        System.out.println(isSorted(ls.selectionSort(getTestArray(1))));
        System.out.println(isSorted(ls.selectionSort(getTestArray(2))));
        System.out.println(isSorted(ls.selectionSort(getTestArray(10))));
        System.out.println(isSorted(ls.selectionSort(getTestArray(100))));
        System.out.println();
        System.out.println("Quick sort: ");
        System.out.println(isSorted(ls.quickSort(getTestArray(0))));
        System.out.println(isSorted(ls.quickSort(getTestArray(1))));
        System.out.println(isSorted(ls.quickSort(getTestArray(2))));
        System.out.println(isSorted(ls.quickSort(getTestArray(10))));
        System.out.println(isSorted(ls.quickSort(getTestArray(100))));
        System.out.println();
        System.out.println("Merge sort: ");
        System.out.println(isSorted(ls.mergeSort(getTestArray(0))));
        System.out.println(isSorted(ls.mergeSort(getTestArray(1))));
        System.out.println(isSorted(ls.mergeSort(getTestArray(2))));
        System.out.println(isSorted(ls.mergeSort(getTestArray(10))));
        System.out.println(isSorted(ls.mergeSort(getTestArray(100))));
        System.out.println();
        System.out.println("Heap sort: ");
        System.out.println(isSorted(ls.heapSort(getTestArray(0))));
        System.out.println(isSorted(ls.heapSort(getTestArray(1))));
        System.out.println(isSorted(ls.heapSort(getTestArray(2))));
        System.out.println(isSorted(ls.heapSort(getTestArray(10))));
        System.out.println(isSorted(ls.heapSort(getTestArray(100))));
        System.out.println();
        System.out.println("Binary search: ");
        System.out.println(ls.binarySearch(new int[]{}, -4) == -1);
        System.out.println(ls.binarySearch(new int[]{4, 7, 34, 35, 94}, 80) == -1);
        System.out.println(ls.binarySearch(new int[]{4, 7, 34, 35, 94}, 94) == 4);
        System.out.println(ls.binarySearch(new int[]{4, 7, 20, 34, 35, 94}, 94) == 5);
        System.out.println(ls.binarySearch(new int[]{4, 7, 20, 34, 35, 94}, 34) == 3);
    }

    static int[] getTestArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < array.length; i++)
            array[i] = (int) (Math.random() * 50);
        return array;
    }

    static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++)
            if (array[i] < array[i - 1])
                return false;
        return true;
    }
}

class ListsPractice extends ListsSolutions {
    //public int[] insertionSort(int[] a){ }

    //public int[] selectionSort(int[] a){ }

    //public int[] quickSort(int[] a){ }

    //public int[] mergeSort(int[] a){ }

    //public int[] heapSort(int[] a) { }

    //public int binarySearch(int[] a, int n) { }
}

abstract class ListsMethods {
    abstract public int[] insertionSort(int[] a);

    abstract public int[] selectionSort(int[] a);

    abstract public int[] quickSort(int[] a);

    abstract public int[] mergeSort(int[] a);

    abstract public int[] heapSort(int[] a);

    abstract public int binarySearch(int[] a, int n);

    public void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}

class ListsSolutions extends ListsMethods {
    public int[] insertionSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = i;
            while (j > 0 && a[j - 1] > a[j]) {
                swap(a, j - 1, j);
                j--;
            }
        }
        return a;
    }

    public int[] selectionSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int smallest = i;
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[smallest])
                    smallest = j;
            swap(a, smallest, i);
        }
        return a;
    }

    public int[] quickSort(int[] a) {
        quickSortInner(a, 0, a.length - 1);
        return a;
    }

    private void quickSortInner(int[] a, int begin, int end) {
        if (begin >= end)
            return;

        int boundary = begin;

        for (int i = begin; i < end; i++) {
            if (a[i] < a[end]) {
                swap(a, i, boundary);
                boundary++;
            }
        }
        swap(a, end, boundary);

        quickSortInner(a, begin, boundary - 1);
        quickSortInner(a, boundary + 1, end);
    }

    public int[] mergeSort(int[] a) {
        mergeSortInner(a, 0, a.length - 1);
        return a;
    }

    private void mergeSortInner(int[] a, int begin, int end) {
        if (begin >= end)
            return;

        int mid = begin + (end - begin) / 2;

        mergeSortInner(a, begin, mid);
        mergeSortInner(a, mid + 1, end);

        int[] left = Arrays.copyOfRange(a, begin, mid + 1);
        int[] right = Arrays.copyOfRange(a, mid + 1, end + 1);

        int il = 0, ir = 0, ia = begin;

        while (il < left.length && ir < right.length) {
            if (left[il] < right[ir]) {
                a[ia] = left[il];
                il++;
            } else {
                a[ia] = right[ir];
                ir++;
            }
            ia++;
        }

        while (il < left.length) {
            a[ia] = left[il];
            il++;
            ia++;
        }

        while (ir < right.length) {
            a[ia] = right[ir];
            ir++;
            ia++;
        }
    }

    public int[] heapSort(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--)
            heapify(a, i, a.length);

        for (int boundary = a.length - 1; boundary > 0; boundary--) {
            swap(a, 0, boundary);
            heapify(a, 0, boundary);
        }

        return a;
    }

    private void heapify(int[] a, int i, int boundary) {
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

        int largest = i;

        if (leftChild < boundary && a[leftChild] > a[largest])
            largest = leftChild;

        if (rightChild < boundary && a[rightChild] > a[largest])
            largest = rightChild;

        if (i != largest) {
            swap(a, i, largest);
            heapify(a, largest, boundary);
        }
    }

    public int binarySearch(int[] a, int n) {
        int begin = 0, end = a.length - 1;

        while (begin <= end) {
            int mid = begin + (end - begin) / 2;

            if (n < a[mid])
                end = mid - 1;
            else if (n > a[mid])
                begin = mid + 1;
            else
                return mid;
        }

        return -1;
    }

}