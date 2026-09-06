package data.compression.algorithms.lzw;

import data.compression.algorithms.CompressionAlgorithm;
import data.compression.bits.BitReader;
import data.compression.bits.BitUtils;
import data.compression.bits.BitWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW implements CompressionAlgorithm {
    private static final int ALPHABET_SIZE = 256;

    @Override
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        byte[] input = Files.readAllBytes(Path.of(inputFilePath));

        Map<List<Byte>, Integer> dictionary = new HashMap<>();

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            dictionary.put(List.of((byte) i), i);
        }

        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            BitWriter writer = new BitWriter(output);

            writer.writeBits(input.length, 32);

            int nextCode = ALPHABET_SIZE;
            int i = 0;

            while (i < input.length) {
                List<Byte> current = new ArrayList<>();

                int code = -1;
                int j = i;

                while (j < input.length) {
                    current.add(input[j]);

                    Integer currentCode = dictionary.get(current);
                    if (currentCode == null) break;

                    code = currentCode;
                    j++;
                }

                int bitCount = BitUtils.bitsNeeded(nextCode - 1);
                writer.writeBits(code, bitCount);

                if (j < input.length) dictionary.put(List.copyOf(current), nextCode++);

                i = j;
            }

            writer.flush();
        }
    }

    @Override
    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        Map<Integer, List<Byte>> dictionary = new HashMap<>();

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            dictionary.put(i, List.of((byte) i));
        }

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(inputFilePath));
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {

            BitReader reader = new BitReader(input);

            int originalLength = reader.readBits(32);
            if (originalLength == 0) return;

            int firstCode = reader.readBits(8);
            if (firstCode == -1) throw new EOFException();

            List<Byte> previous = dictionary.get(firstCode);

            for (byte value : previous) {
                output.write(value & 0xFF);
            }

            int decodedLength = previous.size();
            int nextCode = ALPHABET_SIZE;

            while (decodedLength < originalLength) {
                int bitCount = BitUtils.bitsNeeded(nextCode);
                int code = reader.readBits(bitCount);

                if (code == -1) throw new EOFException();

                List<Byte> current = dictionary.get(code);

                if (current == null) {
                    if (code != nextCode) throw new IOException("Invalid LZW code: " + code);

                    current = new ArrayList<>(previous);
                    current.add(previous.getFirst());
                }

                for (byte value : current) {
                    output.write(value & 0xFF);
                }

                decodedLength += current.size();

                List<Byte> newEntry = new ArrayList<>(previous);
                newEntry.add(current.getFirst());

                dictionary.put(nextCode++, List.copyOf(newEntry));

                previous = current;
            }
        }
    }
}