package data.compression.prefix;

import java.util.Map;

public record PrefixCodeHeader(long originalSize, Map<String, Integer> codeTable) { }