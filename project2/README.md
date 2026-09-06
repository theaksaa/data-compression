# LDPC Codes

Implementation of LDPC code construction, syndrome decoding and Gallager B decoding.

## Build

Requirements:

* Java SDK 21
* Maven

Compile the project with:

```bash
mvn clean compile
```

Run with:

```bash
java -cp target/classes data.compression.Main
```

## Parameters

| Parameter | Value |
| --------- | ----: |
| `n`       |    15 |
| `n - k`   |     9 |
| `wr`      |     5 |
| `wc`      |     3 |
| Seed      | 242021 |
| `th0`     |   0.5 |
| `th1`     |   0.5 |