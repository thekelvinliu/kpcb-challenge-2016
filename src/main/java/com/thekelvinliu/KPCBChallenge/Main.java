package com.thekelvinliu.KPCBChallenge;

import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("include size");
            System.exit(1);
        }
        int s = Integer.parseInt(args[0]);
        FixedSizeHashMap<Integer> fshmI = new FixedSizeHashMap<Integer>(s);
        FixedSizeHashMap<Integer> fshmS = new FixedSizeHashMap<Integer>(s*4);
        // fshmI.inc();
        // for (int i = 0; i < s; i++) fshmS.inc();
        System.out.printf("Integer map load is %f%n", fshmI.load());
        System.out.printf("String map load is %f%n", fshmS.load());
    }
}
