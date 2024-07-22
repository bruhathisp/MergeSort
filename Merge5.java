package MergeSortAlgorithm;
import java.util.Random;

import MergeSortAlgorithm.Merge4.Timer;
public class Merge5{



public static Random random = new Random();
    public static int numTrials = 50;
    public static int threshold = 15; // threshold for insertion sort

    public static void main(String[] args) {
    	System.out.println(threshold);
        int[] sizes = {16_000_000, 32_000_000, 64_000_000, 96_000_000, 128_000_000}; // Values of n to test
        for (int size : sizes) {
            try {
                runTest(size);
            } catch (OutOfMemoryError e) {
                System.out.println("OutOfMemoryError for n = " + size);
                break;
            }
        }
    }
    
    public static void wcInitArray(int[] arr, int start, int sz) {
        if (sz == 1) {
            arr[start] = 1;
            return;
        }
        int lsz = sz / 2;
        wcInitArray(arr, start, lsz);
        wcInitArray(arr, start + lsz, (sz % 2 == 0 ? lsz : lsz + 1));
        for (int i = start; i < start + lsz; i++) {
            arr[i] *= 2;
        }
        for (int i = start + lsz; i < start + sz; i++) {
            arr[i] = arr[i] * 2 - 1;
        }
    }

    public static void runTest(int n) {
        System.out.println("Running tests for n = " + n);
        Timer timer = new Timer();
        long totalDuration = 0;

        for (int i = 0; i < numTrials; i++) {
            int[] arr = new int[n];
            int[] wcInput = new int[n];
            wcInitArray(wcInput, 0, wcInput.length);
            initArray(arr, wcInput);

            timer.start();
            mergeSort(arr);
            timer.end();
            totalDuration += timer.duration();
            System.out.println(i);
        }

        long averageDuration = totalDuration / numTrials;
        System.out.println("Average time for n = " + n + ": " + averageDuration + " msec.");
        System.out.println("Memory used: " + (timer.memory() / 1048576) + " MB");
    }

    //Algorithm Starts here
    
    
    public static void mergeSort(int[] A) {
        int n = A.length;
        int[] B = new int[n];
        System.arraycopy(A, 0, B, 0, n);

        // Apply insertion sort on small chunks
        for (int i = 0; i < n; i += threshold) {
            int right = Math.min(i + threshold - 1, n - 1);
            insertionSort(A, i, right);
        }

        // Merge sorted chunks
        for (int size = threshold; size < n; size *= 2) {
            for (int i = 0; i < n; i += 2 * size) {
                int mid = Math.min(i + size - 1, n - 1);
                int right = Math.min(i + 2 * size - 1, n - 1);
                merge(B, A, i, mid, right);
            }
            int[] temp = A;
            A = B;
            B = temp;
        }

        if (A != B) {
            System.arraycopy(A, 0, B, 0, n);
        }
    }

    private static void insertionSort(int[] A, int p, int r) {
        for (int i = p + 1; i <= r; i++) {
            int key = A[i];
            int j = i - 1;
            while (j >= p && A[j] > key) {
                A[j + 1] = A[j];
                j--;
            }
            A[j + 1] = key;
        }
    }

    private static void merge(int[] A, int[] B, int p, int q, int r) {
        int i = p, j = q + 1, k = p;

        while (i <= q && j <= r) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }

        while (i <= q) {
            A[k++] = B[i++];
        }

        while (j <= r) {
            A[k++] = B[j++];
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    //algorithm ends
    
    
    
    

    // Helper Methods
    public static void initArray(int[] arr, int[] wcInput) {
        System.arraycopy(wcInput, 0, arr, 0, arr.length);
    }

    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() {
            if (!ready) {
                end();
            }
            return elapsedTime;
        }

        public long memory() {
            if (!ready) {
                end();
            }
            return memUsed;
        }
    }
}

    
