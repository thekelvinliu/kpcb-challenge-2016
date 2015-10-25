package com.thekelvinliu.KPCBChallenge;

import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("include size");
            System.exit(1);
        }
        int s = Integer.parseInt(args[0]);
        FixedSizeHashMap<String> a = new FixedSizeHashMap<String>(s);
        a.set("a", "a");
        a.set("c", "c");
        a.set("d", "d");
        a.set("b", "b");
        a.set("f", "f");
        a.set("_", "_");
        a.set("e", "e");
        a.print();
        String x = a.get("d");
        if (x != null) System.out.println(x);
        else System.out.println("null");
        x = a.get("z");
        if (x != null) System.out.println(x);
        else System.out.println("null");
        System.out.printf("The current load if %f%n", a.load());
    }
}
