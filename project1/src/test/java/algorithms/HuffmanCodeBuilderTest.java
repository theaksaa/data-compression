package algorithms;

import data.compression.algorithms.huffman.HuffmanCodeBuilder;
import data.compression.bytes.FileBytes;
import data.compression.symbols.ByteSymbol;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanCodeBuilderTest {

    @Test
    public void testSimpleDistribution() {
        long[] frequencies = new long[256];

        frequencies[0] = 35;
        frequencies[1] = 17;
        frequencies[2] = 17;
        frequencies[3] = 16;
        frequencies[4] = 15;

        FileBytes fileBytes = new FileBytes(frequencies, 100);

        HuffmanCodeBuilder builder = new HuffmanCodeBuilder();

        List<ByteSymbol> symbols = builder.build(fileBytes);

        assertEquals(5, symbols.size());

        double averageLength = 0;

        for (ByteSymbol symbol : symbols) {
            double probability = symbol.getFrequency() / 100.0;
            averageLength += probability * symbol.getCode().length();
        }

        assertEquals(2.3, averageLength, 0.000001);
    }

    @Test
    public void testSingleSymbol() {
        long[] frequencies = new long[256];
        frequencies['A'] = 50;

        FileBytes fileBytes = new FileBytes(frequencies, 50);

        HuffmanCodeBuilder builder = new HuffmanCodeBuilder();

        List<ByteSymbol> symbols = builder.build(fileBytes);

        Assert.assertEquals(1, symbols.size());
        Assert.assertEquals("0", symbols.getFirst().getCode());
    }
}