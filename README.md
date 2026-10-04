# Assignment 2 - Data Structures

Alua Rakhimzhanova, SE-2509

The project has DynamicArray, MyLinkedList and MinHeap. All three store int values.

## Requirements

- Java 17 or newer
- Maven
- Python and matplotlib to rebuild graphs

## Run tests

```text
mvn clean test
```

## Run benchmark

```text
mvn -q compile exec:java
```

Results are saved in `results/results.csv`. Each case has 20 warm-up runs and 5 measured runs. The middle time is saved. The data uses `new Random(42)`.

- W1: 10,000 random reads.
- W2: 1,000 searches, half present and half absent.
- W3: 1,000 insertions and 1,000 removals at the head or middle.
- W4: insert n values into the heap and extract all of them.

## Build graphs

```text
python -m pip install matplotlib
python plot_results.py
```

Graphs are saved in `results/plots/`.

## Files

- `src/main/java/`: structures and benchmark
- `src/test/java/`: JUnit tests
- `results/results.csv`: results
- `results/plots/`: graphs
- `REPORT.pdf`: report

## Repository

https://github.com/al0ni/data-structures-engine

Branch: `main`  
Tag: `v1.0`
