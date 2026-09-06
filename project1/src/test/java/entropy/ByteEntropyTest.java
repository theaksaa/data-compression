package entropy;
import data.compression.entropy.ByteEntropy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ByteEntropyTest {
    @Test
    public void testSingleUniqueByte() throws IOException {
        double entropy = ByteEntropy.calculateEntropy("src/test/resources/single_unique_byte.txt");
        Assertions.assertEquals(0.0, entropy, 0.000001);
    }

    @Test
    public void testTwoEquallyFrequentBytes() throws IOException {
        double entropy = ByteEntropy.calculateEntropy("src/test/resources/two_equal_frequency_bytes.txt");
        Assertions.assertEquals(1.0, entropy, 0.000001);
    }

    @Test
    public void testAll256() throws IOException {
        double entropy = ByteEntropy.calculateEntropy("src/test/resources/all_256.bin");
        Assertions.assertEquals(8.0, entropy, 0.000001);
    }
}
