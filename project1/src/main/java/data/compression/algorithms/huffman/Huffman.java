package data.compression.algorithms.huffman;

import data.compression.prefix.PrefixCodeCompressionAlgorithm;

public class Huffman extends PrefixCodeCompressionAlgorithm {

    public Huffman() {
        super(new HuffmanCodeBuilder());
    }
}