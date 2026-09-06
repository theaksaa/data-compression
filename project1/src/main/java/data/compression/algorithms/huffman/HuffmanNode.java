package data.compression.algorithms.huffman;

import data.compression.symbols.ByteSymbol;

class HuffmanNode {
    private final long frequency;
    private final ByteSymbol symbol;

    private final HuffmanNode left;
    private final HuffmanNode right;

    private final int minSymbolValue;

    public HuffmanNode(ByteSymbol symbol) {
        this.frequency = symbol.getFrequency();
        this.symbol = symbol;

        this.left = null;
        this.right = null;

        this.minSymbolValue = symbol.getValue();
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.frequency = left.getFrequency() + right.getFrequency();
        this.symbol = null;

        this.left = left;
        this.right = right;

        this.minSymbolValue = Math.min(left.getMinSymbolValue(), right.getMinSymbolValue());
    }

    public long getFrequency() {
        return frequency;
    }

    public ByteSymbol getSymbol() {
        return symbol;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public int getMinSymbolValue() {
        return minSymbolValue;
    }

    public boolean isLeaf() {
        return symbol != null;
    }
}