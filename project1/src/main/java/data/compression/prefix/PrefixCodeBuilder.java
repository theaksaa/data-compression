package data.compression.prefix;

import data.compression.bytes.FileBytes;
import data.compression.symbols.ByteSymbol;

import java.util.List;

public interface PrefixCodeBuilder {
    List<ByteSymbol> build(FileBytes fileBytes);
}