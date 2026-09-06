package algorithms;

import data.compression.algorithms.lzw.LZW;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class LZWTest {

    @Test
    public void testCompressAndDecompress() throws Exception {
        Path input = Files.createTempFile("lzw-input", ".bin");
        Path compressed = Files.createTempFile("lzw-compressed", ".lzw");
        Path decompressed = Files.createTempFile("lzw-output", ".bin");

        byte[] original = {
                65, 65, 65, 65,
                66, 66, 66,
                67, 67,
                68
        };

        Files.write(input, original);

        LZW lzw = new LZW();

        lzw.compress(input.toString(), compressed.toString());
        lzw.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }

    @Test
    public void testRepeatedPattern() throws Exception {
        Path input = Files.createTempFile("lzw-repeated-input", ".bin");
        Path compressed = Files.createTempFile("lzw-repeated-compressed", ".lzw");
        Path decompressed = Files.createTempFile("lzw-repeated-output", ".bin");

        byte[] original = new byte[9000];

        for (int i = 0; i < original.length; i++) {
            original[i] = (byte) ('A' + (i % 5));
        }

        Files.write(input, original);

        LZW lzw = new LZW();

        lzw.compress(input.toString(), compressed.toString());
        lzw.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }

    @Test
    public void testVariableBitWidth() throws Exception {
        Path input = Files.createTempFile("lzw-large-input", ".bin");
        Path compressed = Files.createTempFile("lzw-large-compressed", ".lzw");
        Path decompressed = Files.createTempFile("lzw-large-output", ".bin");

        byte[] original = new byte[10000];
        new Random(42).nextBytes(original);

        Files.write(input, original);

        LZW lzw = new LZW();

        lzw.compress(input.toString(), compressed.toString());
        lzw.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }
}