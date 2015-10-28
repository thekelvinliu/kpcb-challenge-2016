package com.thekelvinliu.KPCBChallenge;

import java.io.Console;

public class Main {
    public static void main(String[] args) {
        //variables
        String input, key, value;
        int choice;
        //create console
        Console c = System.console();
        //prompt for size of hash map
        input = c.readLine("Enter the size of the hash map: ");
        //continue prompting if the user input is bad
        while (!input.matches("^[1-9]\\d*$")) {
            c.printf("'%s' is not a positive integer.%n", input);
            input = c.readLine("Try again: ");
        }
        choice = Integer.parseInt(input);
        FixedSizeHashMap<String> hm = new FixedSizeHashMap<String>(choice);
        c.printf("Created a String hash map of size %d.%n", choice);
        //begin interactive looooop
        while (true) {
            //begin prompting menu
            input = "";
            while (!input.matches("^[1-5]$")) {
                printMenu();
                input = c.readLine("Please select an option above: ");
            }
            choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    while (choice != 0) {
                        key = c.readLine("Enter a key: ");
                        value = c.readLine("Enter a value: ");
                        c.printf("'%s' will be associated with '%s'.%n", key, value);
                        input = c.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            if (hm.set(key, value)) {
                                c.printf("%n'%s' successfully associated with '%s'.%n", key, value);
                            } else {
                                System.out.println("\n:( Something went wrong!");
                                c.printf("Check to see if '%s' is already associated with another String.%n", key);
                            }
                        }
                    }
                    break;
                case 2:
                    while (choice != 0) {
                        key = c.readLine("Enter a key: ");
                        c.printf("Lookup the value associated with '%s'.%n", key);
                        input = c.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            value = hm.get(key);
                            if (value != null) {
                                c.printf("%n'%s' is the value associated with '%s'.%n", value, key);
                            } else {
                                c.printf("%n:( '%s' was not found.%n", key);
                            }
                        }
                    }
                    break;
                case 3:
                    while (choice != 0) {
                        key = c.readLine("Enter a key: ");
                        c.printf("Delete '%s' from the hashmap.%n", key);
                        input = c.readLine("Is this correct? (yes/no) ");
                        if (input.toLowerCase().charAt(0) == 'y') {
                            choice = 0;
                            value = hm.delete(key);
                            if (value != null) {
                                c.printf("%n'%s' was deleted from the hash map. It was associated with '%s'.%n", key, value);
                            } else {
                                c.printf("%n:( '%s' was not found.%n", key);
                            }
                        }
                    }
                    break;
                case 4:
                    c.printf("%nThe current load factor is %.3f.%n", hm.load());
                    break;
                case 5:
                default:
                    System.out.println("Thanks for trying this out!");
                    System.exit(0);
            }
        }
    }
    public static void printMenu() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("(1) Insert a key and value");
        System.out.println("(2) Get the value associated with a key");
        System.out.println("(3) Delete a key");
        System.out.println("(4) Show the load factor");
        System.out.println("(5) Exit");
    }
}
