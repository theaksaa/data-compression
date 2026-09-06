package data.compression.algorithms.lz77;

import data.compression.algorithms.CompressionAlgorithm;
import data.compression.bits.BitReader;
import data.compression.bits.BitUtils;
import data.compression.bits.BitWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LZ77 implements CompressionAlgorithm {
    private static final int LITERAL = 0;
    private static final int MATCH = 1;

    private final int windowSize;

    public LZ77(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        byte[] input = Files.readAllBytes(Path.of(inputFilePath));

        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            BitWriter writer = new BitWriter(output);

            writer.writeBits(input.length, 32);

            int distanceBitCount = BitUtils.bitsNeeded(windowSize - 1);
            int lengthBitCount = BitUtils.bitsNeeded(input.length - 1);

            int i = 0;

            while (i < input.length) {
                LZ77Match match = findLongestMatch(input, i);

                if (match.length() == 0) {
                    writer.writeBits(LITERAL, 1);
                    writer.writeBits(input[i] & 0xFF, 8);

                    i++;
                } else {
                    writer.writeBits(MATCH, 1);
                    writer.writeBits(match.distance() - 1, distanceBitCount);
                    writer.writeBits(match.length() - 1, lengthBitCount);

                    i += match.length();
                }
            }

            writer.flush();
        }
    }

    @Override
    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(inputFilePath));
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {

            BitReader reader = new BitReader(input);

            int originalLength = reader.readBits(32);
            if (originalLength <= 0) return;

            int distanceBitCount = BitUtils.bitsNeeded(windowSize - 1);
            int lengthBitCount = BitUtils.bitsNeeded(originalLength - 1);

            List<Byte> decoded = new ArrayList<>();

            while (decoded.size() < originalLength) {
                int type = reader.readBit();

                if (type == -1) throw new EOFException();

                if (type == LITERAL) {
                    int value = reader.readBits(8);

                    decoded.add((byte) value);
                    output.write(value);
                } else {
                    int distance = reader.readBits(distanceBitCount) + 1;
                    int length = reader.readBits(lengthBitCount) + 1;

                    int i = decoded.size();
                    int j = i - distance;

                    for (int s = 0; s < length; s++) {
                        byte value = decoded.get(j + s);

                        decoded.add(value);
                        output.write(value & 0xFF);
                    }
                }
            }
        }
    }

    private LZ77Match findLongestMatch(byte[] input, int i) {
        int searchStart = Math.max(0, i - windowSize);

        int bestDistance = 0;
        int bestLength = 0;

        for (int j = searchStart; j < i; j++) {
            int k = 0;

            while (i + k < input.length && input[j + k] == input[i + k]) k++;

            if (k > bestLength) {
                bestLength = k;
                bestDistance = i - j;
            }
        }

        return new LZ77Match(bestDistance, bestLength);
    }
}