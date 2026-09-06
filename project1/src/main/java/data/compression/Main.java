package data.compression;

import data.compression.algorithms.CompressionAlgorithm;
import data.compression.algorithms.huffman.Huffman;
import data.compression.algorithms.lz77.LZ77;
import data.compression.algorithms.lzw.LZW;
import data.compression.algorithms.shannonfano.ShannonFano;
import data.compression.entropy.ByteEntropy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String operation = args[0];

        try {
            if (operation.equalsIgnoreCase("entropy")) {
                calculateEntropy(args);
                return;
            }

            if (!operation.equalsIgnoreCase("compress") && !operation.equalsIgnoreCase("decompress")) {
                printUsage();
                return;
            }

            processFile(args, operation);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void calculateEntropy(String[] args) throws IOException {
        if (args.length != 2) {
            printUsage();
            return;
        }

        String inputFilePath = args[1];

        double entropy = ByteEntropy.calculateEntropy(inputFilePath);

        System.out.printf("Entropy: %.4f bits/byte%n", entropy);
    }

    private static void processFile(String[] args, String operation) throws IOException {
        if (args.length < 4) {
            printUsage();
            return;
        }

        String algorithmName = args[1];
        String inputFilePath = args[2];
        String outputFilePath = args[3];

        CompressionAlgorithm algorithm = createAlgorithm(algorithmName, args);

        long inputSize = Files.size(Path.of(inputFilePath));

        long start = System.nanoTime();

        if (operation.equalsIgnoreCase("compress")) algorithm.compress(inputFilePath, outputFilePath);
        else algorithm.decompress(inputFilePath, outputFilePath);

        long end = System.nanoTime();

        long outputSize = Files.size(Path.of(outputFilePath));

        if (operation.equalsIgnoreCase("compress")) printCompressionResult(inputSize, outputSize);
        else printDecompressionResult(inputSize, outputSize);

        System.out.printf("Time: %.2f ms%n", (end - start) / 1000000.0);
    }

    private static CompressionAlgorithm createAlgorithm(String name, String[] args) {
        if (name.equalsIgnoreCase("shannonfano")) return new ShannonFano();
        if (name.equalsIgnoreCase("huffman")) return new Huffman();
        if (name.equalsIgnoreCase("lzw")) return new LZW();
        if (name.equalsIgnoreCase("lz77")) {
            if (args.length != 5) {
                throw new IllegalArgumentException("LZ77 requires window size.");
            }

            int windowSize = Integer.parseInt(args[4]);

            return new LZ77(windowSize);
        }

        throw new IllegalArgumentException("Unknown algorithm: " + name);
    }

    private static void printCompressionResult(long originalSize, long compressedSize) {
        System.out.println("Original size: " + originalSize + " bytes");
        System.out.println("Compressed size: " + compressedSize + " bytes");

        if (originalSize == 0) return;

        double ratio = (double) originalSize / compressedSize;
        double saved = (1.0 - (double) compressedSize / originalSize) * 100.0;

        System.out.printf("Compression ratio: %.2f:1%n", ratio);
        System.out.printf("Space saved: %.2f%%%n", saved);
    }

    private static void printDecompressionResult(long compressedSize, long decompressedSize) {
        System.out.println("Compressed size: " + compressedSize + " bytes");
        System.out.println("Decompressed size: " + decompressedSize + " bytes");
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  java -cp target/classes data.compression.Main entropy <input>");
        System.err.println("  java -cp target/classes data.compression.Main compress <algorithm> <input> <output>");
        System.err.println("  java -cp target/classes data.compression.Main decompress <algorithm> <input> <output>");
        System.err.println("  java -cp target/classes data.compression.Main compress lz77 <input> <output> <windowSize>");
        System.err.println("  java -cp target/classes data.compression.Main decompress lz77 <input> <output> <windowSize>");
        System.err.println();
        System.err.println("Algorithms: shannonfano, huffman, lz77, lzw");
    }
}