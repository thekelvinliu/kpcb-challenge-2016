package com.thekelvinliu.KPCBChallenge;
import com.thekelvinliu.KPCBChallenge.FixedSizeHashMapArray;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("include size");
            System.exit(1);
        }
        int s = Integer.parseInt(args[0]);
        FixedSizeHashMapArray<String> a = new FixedSizeHashMapArray<String>(s);
        a.set("b", "b");
        a.set("a", "a");
        a.set("c", "c");
        System.out.printf("The current load if %f%n", a.load());
        a.inorderPrint();
    }
}
