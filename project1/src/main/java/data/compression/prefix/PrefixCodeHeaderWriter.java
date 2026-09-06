package data.compression.prefix;

import data.compression.symbols.ByteSymbol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class PrefixCodeHeaderWriter {

    public static void write(DataOutputStream output, long originalSize, List<ByteSymbol> symbols) throws IOException {
        output.writeLong(originalSize);
        output.writeShort(symbols.size());

        for (ByteSymbol symbol : symbols) {
            String code = symbol.getCode();

            output.writeByte(symbol.getValue());
            output.writeByte(code.length());
            writePackedCode(output, code);
        }
    }

    private static void writePackedCode(DataOutputStream output, String code) throws IOException {
        int currentByte = 0;
        int bitCount = 0;

        for (int i = 0; i < code.length(); i++) {
            char bitChar = code.charAt(i);

            if (bitChar != '0' && bitChar != '1') throw new IllegalArgumentException("Code can contain only 0 and 1");
            int bit = bitChar - '0';

            currentByte = (currentByte << 1) | bit;
            bitCount++;

            if (bitCount == 8) {
                output.writeByte(currentByte);

                currentByte = 0;
                bitCount = 0;
            }
        }

        if (bitCount > 0) {
            currentByte <<= (8 - bitCount);
            output.writeByte(currentByte);
        }
    }
}