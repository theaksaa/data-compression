package data.compression.algorithms.shannonfano;

import data.compression.prefix.PrefixCodeBuilder;
import data.compression.bytes.FileBytes;
import data.compression.symbols.ByteSymbol;
import data.compression.symbols.ByteSymbolFactory;

import java.util.Comparator;
import java.util.List;

public class ShannonFanoCodeBuilder implements PrefixCodeBuilder {

    @Override
    public List<ByteSymbol> build(FileBytes fileBytes) {
        List<ByteSymbol> byteSymbols = ByteSymbolFactory.createFrom(fileBytes);

        byteSymbols.sort(Comparator.comparingLong(ByteSymbol::getFrequency)
                        .reversed()
                        .thenComparingInt(ByteSymbol::getValue)
        );

        if (byteSymbols.isEmpty()) return byteSymbols;
        if (byteSymbols.size() == 1) {
            byteSymbols.getFirst().appendBit('0');
            return byteSymbols;
        }

        assignCodes(byteSymbols, 0, byteSymbols.size() - 1);
        return byteSymbols;
    }

    private void assignCodes(List<ByteSymbol> byteSymbols, int start, int end) {
        if (start >= end) return;

        int splitIndex = findSplitIndex(byteSymbols, start, end);

        for (int i = start; i <= splitIndex; i++) {
            byteSymbols.get(i).appendBit('0');
        }

        for (int i = splitIndex + 1; i <= end; i++) {
            byteSymbols.get(i).appendBit('1');
        }

        assignCodes(byteSymbols, start, splitIndex);
        assignCodes(byteSymbols, splitIndex + 1, end);
    }

    private int findSplitIndex(List<ByteSymbol> byteSymbols, int start, int end) {
        if (start >= end) throw new IllegalArgumentException("At least two symbols are required");

        long totalSum = 0;

        for (int i = start; i <= end; i++) {
            totalSum += byteSymbols.get(i).getFrequency();
        }

        long leftSum = 0;
        long minDifference = Long.MAX_VALUE;
        int bestSplitIndex = start;

        for (int i = start; i < end; i++) {
            leftSum += byteSymbols.get(i).getFrequency();

            long rightSum = totalSum - leftSum;
            long difference = Math.abs(leftSum - rightSum);

            if (difference < minDifference) {
                minDifference = difference;
                bestSplitIndex = i;
            }
        }

        return bestSplitIndex;
    }
}