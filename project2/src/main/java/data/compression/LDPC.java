package data.compression;

import java.util.Random;

public class LDPC {
    private final int n;
    private final int m;
    private final int wr;
    private final int wc;
    private final Random random;

    public LDPC(int n, int m, int wr, int wc, long seed) {
        this.n = n;
        this.m = m;
        this.wr = wr;
        this.wc = wc;
        this.random = new Random(seed);
    }

    public int[][] generate() {
        validateParameters();

        int[][] matrix = new int[m][n];
        int rowsPerGroup = m / wc;

        for (int row = 0; row < rowsPerGroup; row++) {
            for (int column = row * wr; column < (row + 1) * wr; column++) {
                matrix[row][column] = 1;
            }
        }

        for (int group = 1; group < wc; group++) {
            int[] permutation = createPermutation();

            for (int row = 0; row < rowsPerGroup; row++) {
                for (int column = 0; column < n; column++) {
                    matrix[group * rowsPerGroup + row][column] = matrix[row][permutation[column]];
                }
            }
        }

        return matrix;
    }

    private int[] createPermutation() {
        int[] permutation = new int[n];

        for (int i = 0; i < n; i++) {
            permutation[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }

        return permutation;
    }

    private void validateParameters() {
        if (m % wc != 0) throw new IllegalArgumentException("m must be divisible by wc");
        if (n * wc != m * wr) throw new IllegalArgumentException("Invalid LDPC parameters");
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }

            System.out.println();
        }
    }
}