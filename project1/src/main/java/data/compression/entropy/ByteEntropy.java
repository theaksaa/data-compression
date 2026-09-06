package data.compression.entropy;

import data.compression.bytes.FileBytes;
import data.compression.bytes.FileBytesCalculator;

import java.io.IOException;

public class ByteEntropy {
    public static double calculateEntropy(String filePath) throws IOException {
        double entropy = 0;

        FileBytes f = FileBytesCalculator.getFileByteStatistics(filePath);

        for(int i = 0; i < 256; i++) {
            if(f.getFrequency(i) == 0) continue;

            double p = f.getFrequency(i) * 1.0 / f.getTotalBytes();
            entropy -= p * (Math.log(p) / Math.log(2));
        }

        return entropy;
    }
}
