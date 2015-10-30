# kpcb-challenge-2016
My solution to the 2016 KPCB Engineering Fellowship coding challenge.

##About
This repository contains code for a fixed-size hash map in Java.
The hash map associates string keys with any type of value.
However, a single hash map can only hold one type of value.
That is, the values of each hash map object are necessarily homogeneous.
This is done with Java Generics, and at instantiation, the type of values a hash map will hold must be known.

Internally, key-value associations are held in nodes.
These nodes are organized in a self-balancing binary search tree.
But here's the kicker--the tree is implicit.
Upon instantiation, all nodes are pre-allocated into an array.
I did this partly because I thought it made the pre-allocation a lot easier, and it also simplified the node structure.
Additionally, I've never seen a self-balancing binary search tree implemented this way, so I wanted to see if it was doable.

I decided to use an AVL Tree because it guarantees that the `set`, `get`, and `delete` operations take `O(log n)` time.
This time complexity also applies to Red-Black Trees, but I assumed information retrieval time was most important.
AVL Trees beat out Red-Black Trees on this front because of their more rigid balance properties.
But, if information was given on what kinds of applications this hash map would be used for, the type of tree used could definitely change.
For example, for insert intensive applications, Red-Black Trees would be better.
If a small subset of keys were frequently accessed, a Splay Tree would be the way to go.

Finally, I should note that this project is structured to use [Gradle](http://gradle.org/).
Among a lot of other things, Gradle is a dependency manager and build automation tool.
It sets up the correct classpath and sourcepath when compiling classes with external dependencies.
As you'll see below, it can also run tests and generate documentation for you.
On top of that, you don't even _need_ to have it installed on your system!
The root project directory includes a Gradle wrapper.
To run a Gradle task, simply do
```
$ ./gradlew <task>
```
For Windows users, use `./gradlew.bat`.
Of course, if you do have Gradle installed, feel free to forgo using the wrapper and invoke gradle directly.

## Compilation
The most straightforward way to compile the code is with Gradle's `build` task.
This will compile the `.java` files and run the test classes.
To do this, simply run
```
$ ./gradlew build
```
from the root project directory.
Everything will be in `./build`.
Most notably, `./build/libs/FixedSizeHashMap-1.0.0.jar` is an assmebled jar of the compiled classes.
If you run this jar directly with
```
$ java -jar FixedSizeHashMap-1.0.0.jar
```
you'll get an interactive program to test the functionality of the fixed-size hash map with string values.

## Tests
Prior to this, I had never really written any test classes for code.
I wrote tests for the user-facing methods of the hash map, as well as exceptions that should be thrown when given bad input.
I initially planned also to write tests for the internal workings of the implicit tree too, but since they are written as private methods, the test code got real messy, real fast.
Instead, let the user-facing methods indirectly test these private methods also, because the user-facing methods rely heavily on these private methods.
If you only want to run the tests, invoke Gradle with the `test` task.
After doing so, a nice html rundown of the results will be in `./build/reports/tests`.

## Documentation
I've documented pretty much everything using javadoc-style comments.
Running the `javadoc` task will generate html javadocs for the code.
This can be found in `./build/docs/javadoc`.

## Style
These are some of the guidelines that I personally set for myself while writing the code for this class.
- Use javadoc comments for all classes, fields, and methods, regardless of their access level modifier (public, private, etc.)
- The first line of each javadoc comment is a succinct description of the class, field, or method
- Use the `this` keyword when referencing field
- Indent with 4 spaces
- Group methods logically
- In general, pad arithmetic and binary operators with spaces
- `*`, `/`, `//`, and `%` don't need to be padded with spaces
- In general, try to keep lines less than 80 characters long, but don't mess with variable names or operator spacing to achieve this
  - This isn't Python -- it's not the end of the world if a line exceeds 80 characters :)

## Use Me!
If you decide that you want to use my fixed-size hash map implementation, feel free!
Be sure to `import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap` in your code.
When you're compiling, make sure that classes or jar library is in the classpath.
You can set this using the `-cp` flag.
Also note that these files need to be in the classpath when you actual run the program.

For example, consider `Example.java`:
```java
import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap;

public class Example {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please include size.");
            System.exit(1);
        }
        int size = Integer.parseInt(args[0]);
        FixedSizeHashMap<String> uniMap = new FixedSizeHashMap<String>(size);
        String coolPerson = "Kelvin Liu";
        uniMap.set(coolPerson, "NYU Shanghai");
        System.out.printf("%s attends %s.%n", coolPerson, uniMap.get(coolPerson));
    }
}
```
If you wanted to compile and run this code, you would execute the following:
```
$ javac -cp .:/path/to/jar/FixedSizeHashMap-1.0.0.jar Example.java
$ java -cp .:/path/to/jar/FixedSizeHashMap-1.0.0.jar Example <desired-size>
```
