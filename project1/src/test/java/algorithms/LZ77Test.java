package algorithms;

import data.compression.algorithms.lz77.LZ77;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class LZ77Test {

    @Test
    public void testCompressAndDecompress() throws Exception {
        Path input = Files.createTempFile("lz77-input", ".bin");
        Path compressed = Files.createTempFile("lz77-compressed", ".lz77");
        Path decompressed = Files.createTempFile("lz77-output", ".bin");

        byte[] original = {
                65, 65, 65, 65,
                66, 66, 66,
                67, 67,
                68
        };

        Files.write(input, original);

        LZ77 lz77 = new LZ77(256);

        lz77.compress(input.toString(), compressed.toString());
        lz77.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }

    @Test
    public void testRepeatedBytes() throws Exception {
        Path input = Files.createTempFile("lz77-repeated-input", ".bin");
        Path compressed = Files.createTempFile("lz77-repeated-compressed", ".lz77");
        Path decompressed = Files.createTempFile("lz77-repeated-output", ".bin");

        byte[] original = new byte[1000];

        Arrays.fill(original, (byte) 65);

        Files.write(input, original);

        LZ77 lz77 = new LZ77(256);

        lz77.compress(input.toString(), compressed.toString());
        lz77.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }
}