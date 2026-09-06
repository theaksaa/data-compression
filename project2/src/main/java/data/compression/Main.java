package data.compression;

import data.compression.bits.BitUtils;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        LDPC ldpc = new LDPC(15, 9, 5, 3, 242021);

        int[][] matrix = ldpc.generate();

        System.out.println("H matrix:");
        LDPC.printMatrix(matrix);

        Syndrome syndrome = new Syndrome(matrix);

        Map<Integer, int[]> table = syndrome.generate();
        int distance = syndrome.calculateCodeDistance();

        System.out.println();
        System.out.println("Syndrome-corrector table:");
        syndrome.printTable(table);

        System.out.println();
        System.out.println("Code distance: " + distance);

        int[] received = {
                1, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
        };

        int[] decoded = syndrome.decode(received, table);

        System.out.println();
        System.out.print("Received word: ");
        printVector(received);

        System.out.print("Syndrome decoded word: ");
        printVector(decoded);

        GallagerB gallagerB = new GallagerB(matrix, 0.5, 0.5);

        int[] error = gallagerB.findMinimumUncorrectableError(100);

        System.out.println();

        if (error == null) {
            System.out.println("No uncorrectable error found");
        } else {
            System.out.print("Minimum uncorrectable error: ");
            printVector(error);

            System.out.println("Error weight: " + BitUtils.bitCount(error));
            System.out.println("Code distance: " + distance);
        }
    }

    private static void printVector(int[] vector) {
        for (int bit : vector) {
            System.out.print(bit);
        }

        System.out.println();
    }
}