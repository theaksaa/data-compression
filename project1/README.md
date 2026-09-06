# Data Compression

Implementation and comparison of four lossless compression algorithms:

* Shannon-Fano
* Huffman
* LZW
* LZ77

## Build

Requirements:

* Java SDK 21
* Maven

Compile the project with:

```bash
mvn clean compile
```

## Usage

Supported algorithms:

* `shannonfano`
* `huffman`
* `lzw`
* `lz77`

Calculate file entropy:

```bash
java -cp target/classes data.compression.Main entropy <input>
```

Compress a file:

```bash
java -cp target/classes data.compression.Main compress <algorithm> <input> <output>
```

For LZ77, specify the window size:

```bash
java -cp target/classes data.compression.Main compress lz77 <input> <output> 4096
```

Decompress a file:

```bash
java -cp target/classes data.compression.Main decompress <algorithm> <input> <output>
```

Example:

```bash
java -cp target/classes data.compression.Main compress huffman data.txt data.hf
java -cp target/classes data.compression.Main decompress huffman data.hf data-huffman.txt
```

## Results

Test file:

* Size: **7,443,885 bytes**
* Entropy: **4.2439 bits/byte**

| Algorithm    | Compressed Size |  Ratio | Space Saved | Compression | Decompression |
| ------------ | --------------: | -----: | ----------: | ----------: | ------------: |
| Shannon-Fano |     3,984,469 B | 1.87:1 |      46.47% |   266.93 ms |     181.51 ms |
| Huffman      |     3,977,829 B | 1.87:1 |      46.56% |   260.16 ms |     176.09 ms |
| LZW          |     1,559,479 B | 4.77:1 |      79.05% |  1183.29 ms |     452.46 ms |
| LZ77 (4096)  |     5,237,745 B | 1.42:1 |      29.64% |  7397.33 ms |     279.17 ms |
