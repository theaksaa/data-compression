package data.compression.symbols;

import data.compression.bytes.FileBytes;

import java.util.ArrayList;
import java.util.List;

public class ByteSymbolFactory {

    public static List<ByteSymbol> createFrom(FileBytes fileBytes) {
        List<ByteSymbol> byteSymbols = new ArrayList<>();

        for (int i = 0; i < 256; i++) {
            long frequency = fileBytes.getFrequency(i);

            if (frequency == 0) continue;

            byteSymbols.add(new ByteSymbol(i, frequency));
        }

        return byteSymbols;
    }
}