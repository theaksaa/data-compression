package data.compression.algorithms;

import java.io.IOException;

public interface CompressionAlgorithm {
    void compress(String inputFilePath, String outputFilePath) throws IOException;
    void decompress(String inputFilePath, String outputFilePath) throws IOException;
}
