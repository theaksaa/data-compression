package data.compression.prefix;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrefixCodeHeaderReader {

    public static PrefixCodeHeader read(DataInputStream input) throws IOException {
        long originalSize = input.readLong();
        int symbolCount = input.readUnsignedShort();

        if (originalSize < 0) throw new IOException("Invalid original file size");
        if (symbolCount > 256) throw new IOException("Invalid symbol count");

        Map<String, Integer> codeTable = new HashMap<>();

        for (int i = 0; i < symbolCount; i++) {
            int value = input.readUnsignedByte();
            int codeLength = input.readUnsignedByte();

            if (codeLength == 0) throw new IOException("Invalid zero-length prefix code");

            String code = readPackedCode(input, codeLength);

            if (codeTable.put(code, value) != null) throw new IOException("Duplicate prefix code");
        }

        return new PrefixCodeHeader(originalSize, codeTable);
    }

    private static String readPackedCode(DataInputStream input, int codeLength) throws IOException {
        StringBuilder code = new StringBuilder(codeLength);
        int currentByte = 0;

        for (int i = 0; i < codeLength; i++) {
            if (i % 8 == 0) currentByte = input.readUnsignedByte();

            int bitPosition = 7 - (i % 8);
            int bit = (currentByte >> bitPosition) & 1;

            code.append(bit);
        }

        return code.toString();
    }
}