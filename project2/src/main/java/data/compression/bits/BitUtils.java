package data.compression.bits;

public class BitUtils {
    private BitUtils() { }

    public static int bitCount(int value) {
        int count = 0;

        while (value != 0) {
            count += value & 1;
            value >>= 1;
        }

        return count;
    }

    public static int bitCount(int[] vector) {
        int count = 0;

        for (int bit : vector) {
            count += bit;
        }

        return count;
    }
}