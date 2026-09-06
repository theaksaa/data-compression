package data.compression.prefix;

import data.compression.bits.BitReader;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;

public class PrefixCodeDecoder {

    private static class Node {
        private Node zero;
        private Node one;
        private Integer value;
    }

    private final Node root = new Node();

    public PrefixCodeDecoder(Map<String, Integer> codeTable) throws IOException {
        for (Map.Entry<String, Integer> entry : codeTable.entrySet()) {
            addCode(entry.getKey(), entry.getValue());
        }
    }

    private void addCode(String code, int value) throws IOException {
        Node current = root;

        for (int i = 0; i < code.length(); i++) {
            if (current.value != null) throw new IOException("Invalid prefix code table");

            char bit = code.charAt(i);

            if (bit == '0') {
                if (current.zero == null) current.zero = new Node();
                current = current.zero;

            } else if (bit == '1') {
                if (current.one == null) current.one = new Node();
                current = current.one;

            } else throw new IOException("Invalid prefix code");
        }

        if (current.value != null || current.zero != null || current.one != null) throw new IOException("Invalid prefix code table");
        current.value = value;
    }

    public int readSymbol(BitReader bitReader) throws IOException {
        Node current = root;

        while (current.value == null) {
            int bit = bitReader.readBit();

            if (bit == -1) throw new EOFException("Unexpected end of compressed data");

            current = bit == 0 ? current.zero : current.one;

            if (current == null) throw new IOException("Invalid compressed bit sequence");
        }

        return current.value;
    }
}