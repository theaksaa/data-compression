package data.compression.bits;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private final OutputStream outputStream;

    private int currentByte = 0;
    private int bitCount = 0;

    public BitWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1) throw new IllegalArgumentException("Bit must be 0 or 1");

        currentByte = (currentByte << 1) | bit;
        bitCount++;

        if (bitCount == 8) {
            outputStream.write(currentByte);

            currentByte = 0;
            bitCount = 0;
        }
    }

    public void writeBits(int value, int count) throws IOException {
        for (int i = count - 1; i >= 0; i--) {
            writeBit((value >> i) & 1);
        }
    }

    public void writeCode(String code) throws IOException {
        for (int i = 0; i < code.length(); i++) {
            char bit = code.charAt(i);

            if (bit == '0') writeBit(0);
            else if (bit == '1') writeBit(1);
            else throw new IllegalArgumentException("Code can contain only 0 and 1");
        }
    }

    public void flush() throws IOException {
        if (bitCount > 0) {
            currentByte <<= (8 - bitCount);
            outputStream.write(currentByte);

            currentByte = 0;
            bitCount = 0;
        }

        outputStream.flush();
    }
}