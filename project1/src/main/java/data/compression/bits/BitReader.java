package data.compression.bits;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private final InputStream inputStream;

    private int currentByte;
    private int bitsRemaining = 0;

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int readBit() throws IOException {
        if (bitsRemaining == 0) {
            currentByte = inputStream.read();

            if (currentByte == -1) return -1;

            bitsRemaining = 8;
        }

        bitsRemaining--;

        return (currentByte >> bitsRemaining) & 1;
    }

    public int readBits(int count) throws IOException {
        int value = 0;

        for (int i = 0; i < count; i++) {
            int bit = readBit();

            if (bit == -1) return -1;

            value = (value << 1) | bit;
        }

        return value;
    }
}