import java.util.Random;
public class Merge1 {




    public static Random random = new Random();
    public static int numTrials = 50;
    public static int threshold = 16; // threshold for insertion sort

    public static void main(String[] args) {
        int[] sizes = {16_000_000, 32_000_000, 64_000_000, 96_000_000, 512_000_000, 1_000_000_000}; // Values of n to test
        for (int size : sizes) {
            try {
                runTest(size);
            } catch (OutOfMemoryError e) {
                System.out.println("OutOfMemoryError for n = " + size);
                break;
            }
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
            System.out.println("Iteration"+(i + 1) + "\t" );
        }

        long averageDuration = totalDuration / numTrials;
        System.out.println("Average time for n = " + n + ": " + averageDuration + " msec.");
        System.out.println("Memory used: " + (timer.memory() / 1048576) + " MB");
    }
    
//    Merge Sort Algorithm Starts here
    
    public static void mergeSort(int[] A) {
        mergeSort(A, 0, A.length - 1);
    }

    private static void mergeSort(int[] A, int p, int r) {
        if (p < r) {
            int q = p + (r - p) / 2;
            mergeSort(A, p, q);
            mergeSort(A, q + 1, r);
            merge(A, p, q, r);
        }
    }

    private static void merge(int[] A, int p, int q, int r) {
        int[] B = new int[r - p + 1];
        System.arraycopy(A, p, B, 0, r - p + 1);

        int i = 0, k = p, j = q - p + 1;
        while (i <= q - p && j <= r - p) {
            if (B[i] <= B[j]) {
                A[k++] = B[i++];
            } else {
                A[k++] = B[j++];
            }
        }
        while (i <= q - p) {
            A[k++] = B[i++];
        }
        while (j <= r - p) {
            A[k++] = B[j++];
        }
    }
    
//    Merge Sort Algorithm Ends here

    
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
