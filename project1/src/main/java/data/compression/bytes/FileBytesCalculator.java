package data.compression.bytes;

import java.io.FileInputStream;
import java.io.IOException;

public class FileBytesCalculator {
    public static FileBytes getFileByteStatistics(String filePath) throws IOException {
        long[] frequencies = new long[256];
        long totalBytes = 0;

        try (FileInputStream f = new FileInputStream((filePath))) {
            byte[] buffer = new byte[4096];

            int bytesRead;
            while ((bytesRead = f.read(buffer)) != -1) {
                totalBytes += bytesRead;

                for(int i = 0; i < bytesRead; i++) {
                    frequencies[buffer[i] & 0xFF]++;
                }
            }

            return new FileBytes(frequencies, totalBytes);
        }
    }
}
