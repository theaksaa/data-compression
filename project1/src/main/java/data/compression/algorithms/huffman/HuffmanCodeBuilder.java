package data.compression.algorithms.huffman;

import data.compression.prefix.PrefixCodeBuilder;
import data.compression.bytes.FileBytes;
import data.compression.symbols.ByteSymbol;
import data.compression.symbols.ByteSymbolFactory;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class HuffmanCodeBuilder implements PrefixCodeBuilder {

    @Override
    public List<ByteSymbol> build(FileBytes fileBytes) {
        List<ByteSymbol> byteSymbols = ByteSymbolFactory.createFrom(fileBytes);

        if (byteSymbols.isEmpty()) return byteSymbols;

        if (byteSymbols.size() == 1) {
            byteSymbols.getFirst().appendBit('0');
            return byteSymbols;
        }

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(Comparator
                                                                .comparingLong(HuffmanNode::getFrequency)
                                                                .thenComparingInt(HuffmanNode::getMinSymbolValue));

        for (ByteSymbol symbol : byteSymbols) {
            queue.add(new HuffmanNode(symbol));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.remove();
            HuffmanNode right = queue.remove();

            HuffmanNode parent = new HuffmanNode(left, right);

            queue.add(parent);
        }

        HuffmanNode root = queue.remove();

        assignCodes(root, "");

        return byteSymbols;
    }

    private void assignCodes(HuffmanNode node, String code) {
        if (node.isLeaf()) {
            node.getSymbol().setCode(code);
            return;
        }

        assignCodes(node.getLeft(), code + "0");
        assignCodes(node.getRight(), code + "1");
    }
}