# kpcb-challenge-2016
My solution to the 2016 KPCB Engineering Fellowship coding challenge.

##About
This repository contains code for a fixed-size hash map in Java.
The hash map uses string keys.
The values associated with the keys can be of any type, but a single hash map can only hold one type of value.
That is, the values of each hash map object are necessarily homogeneous.
This is done with Java Generics, and at instantiation, the type of values a hash map will hold must be known.

Internally, key-value associations are held in nodes.
These nodes are organized in a self-balancing binary search tree.
But here's the kicker, this tree in implicit.
Upon instantiation, all nodes are pre-allocated into a single array.
I did this because it simplified this pre-allocation as well as the node structure.
Additionally, I haven't ever seen a self-balancing binary search tree implementation that does this, and I wanted to see if it was doable.

The implicit tree is an AVL Tree, meaning the set, get, and delete operations are `O(log n)` on average and in the worst case.
I chose this over the other obvious contenter, a Red-Black Tree, because I assumed information retreival times were most important.
If I had prior knowledge on what applications this hash map would be used for, the type of self-balancing tree would change.
For example, if only a few keys were frequently accessed, using a splay tree would be the way to go.

Finally, I should note that this project is structured to use [Gradle](http://gradle.org/).
Among a lot of other things, Gradle is a dependency manager and build automation tool.
It sets up the correct classpath and sourcepath when compiling classes with external dependencies.
As you'll see below, it can also run tests for you and generate documentation.
On top of that, you don't even _need_ to have it installed on your system.
Included in the root project directory is a Gradle wrapper, `./gradlew` (for Windows users, use `./gradlew.bat`).
This will give you access all Gradle functionality.

## Compilation
The most straightforward way to compile the code is with Gradle's `build` task.
This will compile the `.java` files and run the test classes.
To build, simply run
```
$ ./gradlew build
```
from the root project directory.
Everything will be in `./build`.
Most notably, `./build/libs/FixedSizeHashMap-1.0.0.jar` is an assmebled jar of the compiled classes.
If you run this jar directly with
```
$java -jar FixedSizeHashMap-1.0.0.jar
```
you'll get anan interactive program to test the functionality of the fixed-size hash map with string values.

## Tests
Prior to this, I had never really written any test classes for code.
I wrote tests for the user-facing methods of the hash map, as well as exceptions that should be thrown when given bad input.
I initially planned also to write tests for the internal workings of the implicit tree, which are coded as private methods.
Because the user-facing methods directly rely on these private methods, let the tests as written indirectly test these private methods also.
If you want to only run the tests,
```
./gradlew test
```
is the way to go.
You can also find a nice html rundown of the results at `./build/reports/tests/index.html`.

## Documentation
I've documented pretty much everything using javadoc-style comments.
Running
```
./gradlew javadoc
```
with generate the javadocs.
This can be found at `./build/docs/javadoc/index.html`.

## Style
These are some of the guidelines that I personally set for myself while writing the code for this class.
- Use javadoc comments for all classes, fields, and methods, regardless of their access level modifier (public, private, etc.)
- The first line of each javadoc comment is a succinct description of the class, field, or method
- Use the `this` keyword when referencing field.
- Indent with 4 spaces.
- Group methods logically
- In general, pad arithmetic and binary operators with spaces
- `*`, `/`, `//`, and `%` don't need to be padded with spaces
- In general, try to keep lines less than 80 characters long, but don't mess with variable names or operator spacing to achieve this
  - This isn't Python -- it's not the end of the world if a line exceeds 80 characters :)

## Use Me!
If you decide that you want to use my fixed-size hash map implementation, feel free!
Be sure to `import com.thekelvinliu.KPCBChallenge.FixedSizeHashMap` in your code.
When you're compiling, the jar library needs to be in the classpath.
You can set this using the `-cp` flag.
Also note that the jar library also needs to be in the classpath when you execute the program.
Again, this can be done with the `-cp` flag.

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
