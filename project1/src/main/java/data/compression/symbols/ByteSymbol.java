package data.compression.symbols;

public class ByteSymbol {
    private final int value;
    private final long frequency;
    String code = "";

    public ByteSymbol(int value, long frequency) {
        if (value < 0 || value > 255) throw new IllegalArgumentException("Byte value must be between 0 and 255");
        if (frequency < 0) throw new IllegalArgumentException("Frequency cannot be negative");

        this.value = value;
        this.frequency = frequency;
    }

    public int getValue() {
        return value;
    }

    public long getFrequency() {
        return frequency;
    }

    public String getCode() {
        return code;
    }

    public void appendBit(char bit) {
        if (bit != '0' && bit != '1') throw new IllegalArgumentException("Bit must be 0 or 1");
        code += bit;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
