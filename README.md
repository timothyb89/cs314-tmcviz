cs314-tmcviz
============

A visualizer and debugger for TMCSimulator.

Quickstart
----------

1. Download a .jar from the releases tab, or clone the repository and run `mvn install`
2. Run the jar: `java -jar tmcviz-1.0-SNAPSHOT-with-dependencies.jar`
3. Browse to a compiled jar containing your code
4. Enter a fully-qualified classname for your TMCSimulator
   * This should be detected automatically.
5. Enter a new railmap (if desired), by default it uses the example from the documentation
6. Use the "From file..." button to browse to a valid simulation file.
7. Use the slider on the right hand side of the screen to move between different loop iterations.

How it Works
------------
Your TMCSimulator class is dynamically loaded and wrapped. When a simulation file is executed, the output is captured and parsed, and the result is displayed in the "current state" panel.

This only attempts to display "facts" about your system; that is, no guesses are made as to what your output really means. If your system moves two trains onto the same platform / segment / etc, it will be displayed like that. Similarly, anything not printed is not displayed - trains that reach their destination won't be removed from the display since there isn't a defined message for this.

Pitfalls and Problems
---------------------
* Your output must roughly match the specifications. It's lenient, but not psychic
* Your method names and parameter types in TMCSimulator must exactly match the specification
* You must have a default (i.e. parameterless) constructor in TMCSimulator
* Only events written to the console are displayed
* You (currently) must print to standard out, anything printed to standard error will be ignored
  * Errors are ignored anyway, so these can be printed anywhere
  * This may or may not change in the future
* Only basic movement and map modifications are detected.
  * Trains (currently) are not removed from the graph when they reach their destination

Other things to note:
* A new TMCSimulator instance is created for every execution of "From file..."
* What's displayed is the final state of each turn. This won't show anything if you make multiple changes to lights, etc on a single turn.

Upcoming Features
-----------------
* Show trains (!). The beta currently available in Releases is an alpha / tech demo, and only displays light state. More specifically, train positions are kept track of, but not displayed (yet).
* Individual method execution - see what specific commands do to your railmap

Enhancements
------------
You may add a method like the following to your TMCSimulator class:

```java
public void simulateSingleLoop(String text) {
  // execute one loop from the given text (possibly several lines)
  // this TMCSimulator instance will be reused
}
```

If found, the textbox in the "Output" section will be enabled, allowing you to enter individual commands. Entered lines will be queued and an empty line will submit a loop. **Note: currently only partially implemented**
