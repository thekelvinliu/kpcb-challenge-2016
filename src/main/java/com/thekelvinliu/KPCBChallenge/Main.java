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
        System.out.println(fshmI);
        // int a = 1;
        // if (fshmI.set("a", a++)) System.out.printf("The load is %f%n", fshmI.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmI.set("b", a++)) System.out.printf("The load is %f%n", fshmI.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmI.set("c", a++)) System.out.printf("The load is %f%n", fshmI.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmI.set("d", a++)) System.out.printf("The load is %f%n", fshmI.load());
        // else System.out.println("setting did NOT work.");
        // System.out.printf("the value that corresponds to 'd' is %d%n", fshmI.get("d"));

        // a = 1;
        // FixedSizeHashMap<String> fshmS = new FixedSizeHashMap<String>(3);
        // if (fshmS.set(Integer.toString(a++), "a")) System.out.printf("The load is %f%n", fshmS.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmS.set(Integer.toString(a++), "b")) System.out.printf("The load is %f%n", fshmS.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmS.set(Integer.toString(a++), "c")) System.out.printf("The load is %f%n", fshmS.load());
        // else System.out.println("setting did NOT work.");
        // if (fshmS.set(Integer.toString(a++), "d")) System.out.printf("The load is %f%n", fshmS.load());
        // else System.out.println("setting did NOT work.");
        // System.out.printf("the value that corresponds to '4' is '%s'%n", fshmS.get(Integer.toString(4)));
    }
}
