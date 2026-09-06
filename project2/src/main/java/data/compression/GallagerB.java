package data.compression;

import data.compression.bits.BitUtils;

public class GallagerB {
    private final int[][] matrix;
    private final int n;
    private final int m;
    private final double th0;
    private final double th1;

    public GallagerB(int[][] matrix, double th0, double th1) {
        this.matrix = matrix;
        this.m = matrix.length;
        this.n = matrix[0].length;
        this.th0 = th0;
        this.th1 = th1;
    }

    public int[] decode(int[] received, int maxIterations) {
        validateInput(received, maxIterations);

        int[] current = received.clone();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            if (isCodeword(current)) return current;

            int[] next = new int[n];

            for (int column = 0; column < n; column++) {
                int zeros = 0;
                int ones = 0;
                int neighbors = 0;

                for (int row = 0; row < m; row++) {
                    if (matrix[row][column] == 0) continue;

                    int message = 0;

                    for (int otherColumn = 0; otherColumn < n; otherColumn++) {
                        if (otherColumn == column) continue;
                        if (matrix[row][otherColumn] == 0) continue;

                        message ^= current[otherColumn];
                    }

                    if (message == 0) zeros++;
                    else ones++;

                    neighbors++;
                }

                if (zeros >= th0 * neighbors) next[column] = 0;
                else if (ones >= th1 * neighbors) next[column] = 1;
                else next[column] = received[column];
            }

            current = next;
        }

        if (isCodeword(current)) return current;

        return null;
    }

    private boolean isCodeword(int[] vector) {
        for (int row = 0; row < m; row++) {
            int parity = 0;

            for (int column = 0; column < n; column++) {
                parity ^= matrix[row][column] & vector[column];
            }

            if (parity != 0) return false;
        }

        return true;
    }

    private void validateInput(int[] received, int maxIterations) {
        if (received.length != n) throw new IllegalArgumentException("Invalid vector length");
        if (th0 < 0 || th0 > 1) throw new IllegalArgumentException("th0 must be between 0 and 1");
        if (th1 < 0 || th1 > 1) throw new IllegalArgumentException("th1 must be between 0 and 1");
        if (maxIterations <= 0) throw new IllegalArgumentException("maxIterations must be positive");

        for (int bit : received) {
            if (bit != 0 && bit != 1) throw new IllegalArgumentException("Vector must be binary");
        }
    }

    public int[] findMinimumUncorrectableError(int maxIterations) {
        int limit = 1 << n;

        for (int weight = 1; weight <= n; weight++) {
            for (int value = 1; value < limit; value++) {
                if (BitUtils.bitCount(value) != weight) continue;

                int[] error = toVector(value);
                int[] decoded = decode(error, maxIterations);

                if (decoded == null || !isZeroVector(decoded)) {
                    return error;
                }
            }
        }

        return null;
    }

    private boolean isZeroVector(int[] vector) {
        for (int bit : vector) {
            if (bit != 0) return false;
        }

        return true;
    }

    private int[] toVector(int value) {
        int[] vector = new int[n];

        for (int i = 0; i < n; i++) {
            vector[i] = (value >> (n - 1 - i)) & 1;
        }

        return vector;
    }
}