# kpcb-challenge-2016
My solution to the 2016 KPCB Engineering Fellowship coding challenge.

##About
I enjoyed doing this.
Had to decide between python and java, but ultimately decided java because types, gradle, and I like it.
throwback to data structures.
organized code and used [Gradle](http://gradle.org/).
Gradle wrapper is included if your system does not have gradle installed

## Compilation
The most straightforward way to compile the code is with Gradle's `build` task. This will compile the `.java` files and run the test classes.
To build, simply run `./gradlew build` from the project root directory.
The results will be in `./build`.

## Tests
`./gradlew test` runs the individual tests.
A nice html rundown of what happened can be found at `./build/reports/tests/index.html`.

## Documentation
`./gradlew javadoc` generates the javadoc.
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
