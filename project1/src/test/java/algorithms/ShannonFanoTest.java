package algorithms;

import data.compression.algorithms.shannonfano.ShannonFano;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ShannonFanoTest {

    @Test
    public void testCompressAndDecompress() throws Exception {
        Path input = Files.createTempFile("sf-input", ".bin");
        Path compressed = Files.createTempFile("sf-compressed", ".sf");
        Path decompressed = Files.createTempFile("sf-output", ".bin");

        byte[] original = {
                65, 65, 65, 65,
                66, 66, 66,
                67, 67,
                68
        };

        Files.write(input, original);

        ShannonFano shannonFano = new ShannonFano();

        shannonFano.compress(input.toString(), compressed.toString());

        shannonFano.decompress(compressed.toString(), decompressed.toString());

        byte[] decoded = Files.readAllBytes(decompressed);

        assertArrayEquals(original, decoded);
    }
}