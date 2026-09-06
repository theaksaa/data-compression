package data.compression.bits;

public class BitUtils {
    public static int bitsNeeded(int value) {
        if (value <= 0) return 1;
        return 32 - Integer.numberOfLeadingZeros(value);
    }
}
