package bits;

import data.compression.bits.BitWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BitWriterTest {

    @Test
    public void testWriteOneByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        bitWriter.writeCode("11001010");
        bitWriter.flush();

        byte[] result = outputStream.toByteArray();

        assertEquals(1, result.length);
        assertEquals(0b11001010, result[0] & 0xFF);
    }

    @Test
    public void testWritePartialByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        bitWriter.writeCode("101");
        bitWriter.flush();

        byte[] result = outputStream.toByteArray();

        assertEquals(1, result.length);
        assertEquals(0b10100000, result[0] & 0xFF);
    }

    @Test
    public void testMultipleWriteCodeCalls() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        bitWriter.writeCode("110");
        bitWriter.writeCode("1");
        bitWriter.writeCode("1011");

        bitWriter.flush();

        byte[] result = outputStream.toByteArray();

        assertEquals(1, result.length);
        assertEquals(0b11011011, result[0] & 0xFF);
    }

    @Test
    public void testWriteMoreByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        bitWriter.writeCode("11110000101");
        bitWriter.flush();

        byte[] result = outputStream.toByteArray();

        assertEquals(2, result.length);

        assertEquals(0b11110000, result[0] & 0xFF);
        assertEquals(0b10100000, result[1] & 0xFF);
    }

    @Test
    public void testEmptyCode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        bitWriter.writeCode("");
        bitWriter.flush();

        byte[] result = outputStream.toByteArray();

        assertEquals(0, result.length);
    }

    @Test
    public void testInvalidCode() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitWriter bitWriter = new BitWriter(outputStream);

        assertThrows(IllegalArgumentException.class, () -> bitWriter.writeCode("101201"));
    }
}