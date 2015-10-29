package com.thekelvinliu.KPCBChallenge;

import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

public class FixedSizeHashMapTest {
    //bounds for the size of the hash map
    private static final int MIN_SIZE = 500;
    private static final int MAX_SIZE = 15000;
    private static Random rg;
    private static int size;
    private FixedSizeHashMap<Integer> intMap;
    private FixedSizeHashMap<String> strMap;
    private FixedSizeHashMap<Boolean> boolMap;

    @BeforeClass
    public static void setup() {
        rg = new Random();
        size = rg.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
        System.out.printf("The size for this test is %d.%n", size);
    }

    @Before
    public void initializeHashMaps() {
        intMap = new FixedSizeHashMap<Integer>(size);
        strMap = new FixedSizeHashMap<String>(size);
        boolMap = new FixedSizeHashMap<Boolean>(size);
    }

    @Test
    public void testSet() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rg.nextInt();
            assertTrue(intMap.set(k, v));
            assertTrue(strMap.set(k, Integer.toString(v)));
            assertTrue(boolMap.set(k, v%2 == 0));
        }
    }

    @Test
    public void testSetDuplicateKey() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rg.nextInt();
            assertTrue(intMap.set(k, v));
            assertTrue(strMap.set(k, Integer.toString(v)));
            assertTrue(boolMap.set(k, v%2 == 0));
            assertFalse(intMap.set(k, v));
            assertFalse(strMap.set(k, Integer.toString(v)));
            assertFalse(boolMap.set(k, v%2 == 0));
        }
    }

    @Test
    public void testGet() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rg.nextInt();
            if (intMap.set(k, v)) {
                assertEquals((int)v, (int)intMap.get(k));
            }
            if (strMap.set(k, Integer.toString(v))) {
                assertEquals(Integer.toString(v), strMap.get(k));
            }
            if (boolMap.set(k, v%2 == 0)) {
                assertEquals(v%2 == 0, boolMap.get(k));
            }
        }
    }

    @Test
    public void testDelete() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rg.nextInt();
            if (intMap.set(k, v)) {
                assertEquals((int)intMap.delete(k), (int)v);
                assertEquals(0.0, intMap.load(), 0.00001);
            }
            if (strMap.set(k, Integer.toString(v))) {
                assertEquals(strMap.delete(k), Integer.toString(v));
                assertEquals(0.0, strMap.load(), 0.00001);
            }
            if (boolMap.set(k, v%2 == 0)) {
                assertEquals(boolMap.delete(k), v%2 == 0);
                assertEquals(0.0, boolMap.load(), 0.00001);
            }
        }
    }

    @Test
    public void testLoad() {
        for (int i = 0; i < size; i++) {
            String k = "test_key_" + Integer.toString(i);
            int v = rg.nextInt();
            if (intMap.set(k, v)) {
                assertEquals((float)(i + 1)/size, intMap.load(), 0.00001);
            }
            if (strMap.set(k, Integer.toString(v))) {
                assertEquals((float)(i + 1)/size, strMap.load(), 0.00001);
            }
            if (boolMap.set(k, v%2 == 0)) {
                assertEquals((float)(i + 1)/size, boolMap.load(), 0.00001);
            }
        }
    }
}
