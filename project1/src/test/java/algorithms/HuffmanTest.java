package algorithms;

import data.compression.algorithms.huffman.Huffman;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuffmanTest {

    @Test
    public void testCompressAndDecompress() throws Exception {
        Path input = Files.createTempFile("huffman-input", ".bin");
        Path compressed = Files.createTempFile("huffman-compressed", ".bin");
        Path decompressed = Files.createTempFile("huffman-output", ".bin");

        byte[] original = {
                65, 65, 65, 65, 65,
                66, 66, 66,
                67, 67,
                68
        };

        Files.write(input, original);

        Huffman huffman = new Huffman();

        huffman.compress(input.toString(), compressed.toString());

        huffman.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertTrue(Files.size(compressed) > 0);
        assertArrayEquals(original, decoded);
    }
}