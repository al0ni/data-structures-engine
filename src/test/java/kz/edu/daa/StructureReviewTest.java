package kz.edu.daa;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class StructureReviewTest {
    @Test
    void arrayInsertionAndRemovalAtEverySmallIndex() {
        checkEveryIndex(false);
    }

    @Test
    void listInsertionAndRemovalAtEverySmallIndex() {
        checkEveryIndex(true);
    }

    @Test
    void arrayMatchesModelAfterEveryChange() {
        checkMixedChanges(false);
    }

    @Test
    void listMatchesModelAfterEveryChange() {
        checkMixedChanges(true);
    }

    @Test
    void heapHandlesAllShortSequencesWithDuplicates() {
        for (int code = 0; code < 2187; code++) {
            int[] values = new int[7];
            int remaining = code;
            for (int i = 0; i < values.length; i++) {
                values[i] = remaining % 3 - 1;
                remaining /= 3;
            }
            checkHeap(values);
        }
    }

    @Test
    void heapHandlesIncreasingDecreasingAndEqualValues() {
        for (int order = 0; order < 3; order++) {
            int[] values = new int[1000];
            for (int i = 0; i < values.length; i++) {
                values[i] = order == 0 ? i : order == 1 ? -i : 7;
            }
            checkHeap(values);
        }
    }

    @Test
    void heapCountsGrowthAndMultipleBubbleDownLevels() {
        MinHeap heap = new MinHeap();
        for (int i = 1; i <= 4; i++) {
            heap.insert(i);
        }
        heap.metrics().reset();
        heap.insert(5);
        assertEquals(6, heap.metrics().steps);
        assertEquals(4, heap.metrics().moves);
        assertEquals(1, heap.metrics().comparisons);
        heap.metrics().reset();
        assertEquals(1, heap.extractMin());
        assertEquals(12, heap.metrics().steps);
        assertEquals(5, heap.metrics().moves);
        assertEquals(3, heap.metrics().comparisons);
        checkHeapProperty(heap);
    }

    @Test
    void invalidOperationsDoNotChangeListsOrCounters() {
        for (IntList list : new IntList[]{new DynamicArray(), new MyLinkedList()}) {
            list.add(4);
            list.add(8);
            list.metrics().reset();
            for (int index : new int[]{Integer.MIN_VALUE, -1, 3, Integer.MAX_VALUE}) {
                assertThrows(IndexOutOfBoundsException.class, () -> list.add(index, 9));
                assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
                assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
            }
            assertEquals(0, list.metrics().steps);
            assertEquals(0, list.metrics().moves);
            assertEquals(0, list.metrics().comparisons);
            assertEquals(2, list.size());
            assertEquals(4, list.get(0));
            assertEquals(8, list.get(1));
        }
    }

    private void checkEveryIndex(boolean linked) {
        for (int n = 0; n <= 40; n++) {
            for (int index = 0; index <= n; index++) {
                IntList list = linked ? new MyLinkedList() : new DynamicArray();
                for (int i = 0; i < n; i++) {
                    list.add(i - 20);
                }
                list.add(index, 99);
                assertEquals(n + 1, list.size());
                for (int i = 0; i <= n; i++) {
                    int expected = i < index ? i - 20 : i == index ? 99 : i - 21;
                    assertEquals(expected, list.get(i));
                }
                assertEquals(99, list.remove(index));
                assertEquals(n, list.size());
                for (int i = 0; i < n; i++) {
                    assertEquals(i - 20, list.get(i));
                }
            }
        }
    }

    private void checkMixedChanges(boolean linked) {
        for (int seed = 0; seed < 8; seed++) {
            IntList list = linked ? new MyLinkedList() : new DynamicArray();
            ArrayList<Integer> expected = new ArrayList<>();
            Random random = new Random(seed);
            for (int step = 0; step < 2000; step++) {
                if (expected.isEmpty() || random.nextBoolean()) {
                    int index = random.nextInt(expected.size() + 1);
                    int value = random.nextInt();
                    list.add(index, value);
                    expected.add(index, value);
                } else {
                    int index = random.nextInt(expected.size());
                    assertEquals(expected.remove(index).intValue(), list.remove(index));
                }
                assertEquals(expected.size(), list.size());
                for (int i = 0; i < expected.size(); i++) {
                    assertEquals(expected.get(i).intValue(), list.get(i));
                }
                int query = random.nextInt();
                assertEquals(expected.contains(query), list.contains(query));
            }
            while (!expected.isEmpty()) {
                assertEquals(expected.remove(0).intValue(), list.remove(0));
            }
            list.add(123);
            assertEquals(123, list.remove(0));
            assertEquals(0, list.size());
        }
    }

    private void checkHeap(int[] values) {
        MinHeap heap = new MinHeap();
        for (int value : values) {
            heap.insert(value);
            checkHeapProperty(heap);
        }
        Arrays.sort(values);
        for (int value : values) {
            assertEquals(value, heap.peekMin());
            assertEquals(value, heap.extractMin());
            checkHeapProperty(heap);
        }
        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);
    }

    private void checkHeapProperty(MinHeap heap) {
        int[] values = heap.snapshot();
        for (int i = 1; i < heap.size(); i++) {
            assertTrue(values[(i - 1) / 2] <= values[i]);
        }
    }
}
