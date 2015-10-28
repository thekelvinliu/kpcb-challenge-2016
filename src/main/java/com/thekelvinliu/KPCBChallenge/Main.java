package com.thekelvinliu.KPCBChallenge;

import java.io.Console;
import java.util.Scanner;
import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap;

public class Main {
    public static void main(String[] args) {
        //create console
        Console c = System.console();
        //prompt for size of hash map
        String input = c.readLine("Enter the size of the hash map: ");
        //continue prompting if the user input is bad
        while (!input.matches("^[1-9]\\d*$")) {
            c.printf("'%s' is not a positive integer.%n", input);
            input = c.readLine("Try again: ");
        }
        int size = Integer.parseInt(input);
        FixedSizeHashMap<String> hm = new FixedSizeHashMap<String>(size);
        c.printf("Created a String hash map of size %d.%n", size);
    }
}
