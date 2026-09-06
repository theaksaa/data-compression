package data.compression;

import data.compression.bits.BitUtils;

import java.util.Map;
import java.util.TreeMap;

public class Syndrome {
    private final int[][] matrix;
    private final int n;
    private final int m;

    public Syndrome(int[][] matrix) {
        this.matrix = matrix;
        this.m = matrix.length;
        this.n = matrix[0].length;
    }

    public Map<Integer, int[]> generate() {
        Map<Integer, int[]> table = new TreeMap<>();
        int limit = 1 << n;
        int syndromeCount = 1 << m;

        for (int weight = 0; weight <= n; weight++) {
            for (int value = 0; value < limit; value++) {
                if (BitUtils.bitCount(value) != weight) continue;

                int syndrome = calculateSyndrome(value);

                if (!table.containsKey(syndrome)) {
                    table.put(syndrome, toVector(value));

                    if (table.size() == syndromeCount) return table;
                }
            }
        }

        return table;
    }

    public int calculateCodeDistance() {
        int limit = 1 << n;

        for (int weight = 1; weight <= n; weight++) {
            for (int value = 1; value < limit; value++) {
                if (BitUtils.bitCount(value) != weight) continue;
                if (calculateSyndrome(value) == 0) return weight;
            }
        }

        return -1;
    }

    private int calculateSyndrome(int value) {
        int syndrome = 0;

        for (int row = 0; row < m; row++) {
            int bit = 0;

            for (int column = 0; column < n; column++) {
                int vectorBit = (value >> (n - 1 - column)) & 1;
                bit ^= matrix[row][column] & vectorBit;
            }

            syndrome = (syndrome << 1) | bit;
        }

        return syndrome;
    }

    private int calculateSyndrome(int[] vector) {
        int syndrome = 0;

        for (int row = 0; row < m; row++) {
            int bit = 0;

            for (int column = 0; column < n; column++) {
                bit ^= matrix[row][column] & vector[column];
            }

            syndrome = (syndrome << 1) | bit;
        }

        return syndrome;
    }

    private int[] toVector(int value) {
        int[] vector = new int[n];

        for (int i = 0; i < n; i++) {
            vector[i] = (value >> (n - 1 - i)) & 1;
        }

        return vector;
    }

    public void printTable(Map<Integer, int[]> table) {
        for (Map.Entry<Integer, int[]> entry : table.entrySet()) {
            for (int i = m - 1; i >= 0; i--) {
                System.out.print((entry.getKey() >> i) & 1);
            }

            System.out.print(" -> ");

            for (int bit : entry.getValue()) {
                System.out.print(bit);
            }

            System.out.println();
        }
    }

    public int[] decode(int[] received, Map<Integer, int[]> table) {
        if (received.length != n) {
            throw new IllegalArgumentException("Invalid vector length");
        }

        int syndrome = calculateSyndrome(received);
        int[] error = table.get(syndrome);
        int[] decoded = new int[n];

        for (int i = 0; i < n; i++) {
            decoded[i] = received[i] ^ error[i];
        }

        return decoded;
    }

}