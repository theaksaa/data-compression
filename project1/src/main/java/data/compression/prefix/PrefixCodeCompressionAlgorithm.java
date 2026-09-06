package data.compression.prefix;

import data.compression.algorithms.CompressionAlgorithm;
import data.compression.bits.BitReader;
import data.compression.bits.BitWriter;
import data.compression.bytes.FileBytes;
import data.compression.bytes.FileBytesCalculator;
import data.compression.symbols.ByteSymbol;

import java.io.*;
import java.util.List;

public abstract class PrefixCodeCompressionAlgorithm implements CompressionAlgorithm {

    private final PrefixCodeBuilder codeBuilder;

    protected PrefixCodeCompressionAlgorithm(PrefixCodeBuilder codeBuilder) {
        this.codeBuilder = codeBuilder;
    }

    private String[] createCodeTable(List<ByteSymbol> symbols) {
        String[] codeTable = new String[256];

        for (ByteSymbol symbol : symbols) {
            codeTable[symbol.getValue()] = symbol.getCode();
        }

        return codeTable;
    }

    @Override
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        FileBytes fileBytes = FileBytesCalculator.getFileByteStatistics(inputFilePath);

        List<ByteSymbol> symbols = codeBuilder.build(fileBytes);
        String[] codeTable = createCodeTable(symbols);

        try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)));
             BufferedInputStream input = new BufferedInputStream(new FileInputStream(inputFilePath))) {

            PrefixCodeHeaderWriter.write(output, fileBytes.getTotalBytes(), symbols);
            BitWriter bitWriter = new BitWriter(output);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    int value = buffer[i] & 0xFF;

                    String code = codeTable[value];
                    bitWriter.writeCode(code);
                }
            }

            bitWriter.flush();
        }
    }

    @Override
    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFilePath)));
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {

            PrefixCodeHeader header = PrefixCodeHeaderReader.read(input);

            if (header.originalSize() == 0) return;
            if (header.codeTable().isEmpty()) throw new IOException("Compressed file contains no prefix codes");

            PrefixCodeDecoder decoder = new PrefixCodeDecoder(header.codeTable());
            BitReader bitReader = new BitReader(input);

            for (long i = 0; i < header.originalSize(); i++) {
                int value = decoder.readSymbol(bitReader);
                output.write(value);
            }
        }
    }
}