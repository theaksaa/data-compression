package algorithms;

import data.compression.algorithms.shannonfano.ShannonFanoCodeBuilder;
import data.compression.bytes.FileBytes;
import data.compression.symbols.ByteSymbol;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShannonFanoCodeBuilderTest {

    @Test
    public void testSimpleDistribution() {
        long[] frequencies = new long[256];

        frequencies['A'] = 50;
        frequencies['B'] = 25;
        frequencies['C'] = 10;
        frequencies['D'] = 5;

        FileBytes fileBytes = new FileBytes(frequencies, 100);

        ShannonFanoCodeBuilder builder = new ShannonFanoCodeBuilder();

        List<ByteSymbol> symbols = builder.build(fileBytes);

        assertEquals(4, symbols.size());

        assertEquals('A', symbols.get(0).getValue());
        assertEquals("0", symbols.get(0).getCode());

        assertEquals('B', symbols.get(1).getValue());
        assertEquals("10", symbols.get(1).getCode());

        assertEquals('C', symbols.get(2).getValue());
        assertEquals("110", symbols.get(2).getCode());

        assertEquals('D', symbols.get(3).getValue());
        assertEquals("111", symbols.get(3).getCode());
    }

    @Test
    public void testSingleSymbol() {
        long[] frequencies = new long[256];
        frequencies['A'] = 50;

        FileBytes fileBytes = new FileBytes(frequencies, 50);

        ShannonFanoCodeBuilder builder = new ShannonFanoCodeBuilder();

        List<ByteSymbol> symbols = builder.build(fileBytes);

        assertEquals(1, symbols.size());
        assertEquals("0", symbols.getFirst().getCode());
    }
}
