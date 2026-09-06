package data.compression.algorithms.shannonfano;

import data.compression.prefix.PrefixCodeCompressionAlgorithm;

public class ShannonFano extends PrefixCodeCompressionAlgorithm {

    public ShannonFano() {
        super(new ShannonFanoCodeBuilder());
    }
}