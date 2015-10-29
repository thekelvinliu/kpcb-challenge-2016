package com.thekelvinliu.KPCBChallenge;

import java.util.Random;
import org.junit.*;

public class FixedSizeHashMapExceptionsTest {
    private FixedSizeHashMap<String> strMap;

    @Test(expected = IllegalArgumentException.class)
    public void negativeSizeInstantiation() {
        strMap = new FixedSizeHashMap<String>(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroSizeInstantiation() {
        strMap = new FixedSizeHashMap<String>(0);
    }
}
