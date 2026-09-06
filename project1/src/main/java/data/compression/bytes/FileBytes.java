package data.compression.bytes;

public class FileBytes {
    private final long[] frequencies;
    private final long totalBytes;

    public FileBytes(long[] frequencies, long totalBytes) {
        this.frequencies = frequencies;
        this.totalBytes = totalBytes;
    }

    public long getFrequency(int index) {
        if (index < 0 || index >= frequencies.length) throw new IllegalArgumentException("index out of range");
        return frequencies[index];
    }

    public long getTotalBytes() {
        return totalBytes;
    }
}
