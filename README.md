# 🎄🎁🎅 2022 Advent of Code in Kotlin 🎅🎁🎄

## Project goals

The project goal is to deliver some pretty, readable and concise solutions to Advent of Code 2022 problems all written
in Kotlin language. It should show the other developer how some constructions from the language can be used and how to
solve some kind of tricky problems that appear during the Advent of Code.

## Problems source

You can find all problems at the [page of Advent of Code 2022](https://adventofcode.com/2022). The description of each
problem contains some sample test data, but I also included my input data files from the contest in
the [resources'](./src/main/resources/input) directory of the project to make my project working with some sample, real
world data.

## Solution template

When soling each day problem I use my template of `AdventDay` that I defined
in [AdventDay.kt](./src/main/kotlin/AdventDay.kt) - it's worth looking into this definition by yourself, and also you
can read more about it [at my blog](https://kotlin-dev.ml/post/advent-of-code-2020-0/).

It's enough to create some Kotlin `object` that inherits from `AdventDay` to get the solution running. If you're
interested in the details, let's look into the definition of the [Advent.kt](./src/main/kotlin/Advent.kt)
to see how to find all `object` classes in Kotlin and run some method on them 😎.

To run all solutions, simply call `./gradlew run`

### Tests

The solutions are tested with the values gathered from [adventofcode.com](https://adventofcode.com/2022). Every day of
Advent has its expected output defined in [AdventTest.kt](./src/test/kotlin/AdventTest.kt) and in order to
verify correct state of the outputs for days we run every day solution and catch it standard output stream
to compare it with expected value.

To run all tests, simply call `./gradlew test`

## Problems

The problems solutions are included in project, but for every of them you can also find some corresponding article at my
website, where I discuss not only the given problem, but also some cool features of Kotlin or I deep dive into some
language constructions.

| Problem                                                                 | Solution                               | Tags                                                                                                           |
|-------------------------------------------------------------------------|----------------------------------------|----------------------------------------------------------------------------------------------------------------|
| [Day 1: Calorie Counting](https://adventofcode.com/2022/day/1)          | [Day1.kt](./src/main/kotlin/Day1.kt)   | `sortedDescending`, `groupSeparatedBy`                                                                         |
| [Day 2: Rock Paper Scissors](https://adventofcode.com/2022/day/2)       | [Day2.kt](./src/main/kotlin/Day2.kt)   | `enum class`, `infix fun`                                                                                      |
| [Day 3: Rucksack Reorganization](https://adventofcode.com/2022/day/3)   | [Day3.kt](./src/main/kotlin/Day3.kt)   | `intersect`, `windowed`, `..`                                                                                  |
| [Day 4: Camp Cleanup](https://adventofcode.com/2022/day/4)              | [Day4.kt](./src/main/kotlin/Day4.kt)   | `infix fun`                                                                                                    |
| [Day 5: Supply Stacks](https://adventofcode.com/2022/day/5)             | [Day5.kt](./src/main/kotlin/Day5.kt)   | `typealias`, `Stack<T>`, `operator fun invoke`                                                                 |
| [Day 6: Tuning Trouble](https://adventofcode.com/2022/day/6)            | [Day6.kt](./src/main/kotlin/Day6.kt)   | `windowed`, `mapIndexedNotNull`, `asSequence`                                                                  |
| [Day 7: No Space Left On Device](https://adventofcode.com/2022/day/7)   | [Day7.kt](./src/main/kotlin/Day7.kt)   | `groupSeparatedBy`, filesystem tree, `fold`, graph search                                                      |
| [Day 8: Treetop Tree House](https://adventofcode.com/2022/day/8)        | [Day8.kt](./src/main/kotlin/Day8.kt)   | `IntProgression`, `digitToIntOrNull`, `LazyDefaultMap<K, V>`                                                   |
| [Day 9: Rope Bridge](https://adventofcode.com/2022/day/9)               | [Day9.kt](./src/main/kotlin/Day9.kt)   | simulation, named for, `downTo`, `until`, `operator fun plus`, `operator fun minus`, `operator fun unaryMinus` |
| [Day 10: Cathode-Ray Tube](https://adventofcode.com/2022/day/10)        | [Day10.kt](./src/main/kotlin/Day10.kt) | `repeat`, `private set`, `sealed interface`                                                                    |
| [Day 11: Monkey in the Middle](https://adventofcode.com/2022/day/11)    | [Day11.kt](./src/main/kotlin/Day11.kt) | `repeat`, `private set`, `sealed interface`, modulo operation                                                  |
| [Day 12: Hill Climbing Algorithm](https://adventofcode.com/2022/day/12) | [Day12.kt](./src/main/kotlin/Day12.kt) | `getOrNull`, dijkstra, graph search                                                                            |
| [Day 13: Distress Signal](https://adventofcode.com/2022/day/13)         | [Day13.kt](./src/main/kotlin/Day13.kt) | `Comparator<String>`, recursion                                                                                |
| [Day 14: Regolith Reservoir](https://adventofcode.com/2022/day/14)      | [Day14.kt](./src/main/kotlin/Day14.kt) | `LazyDefaultMap<K, V>`, `directedTo`, `operator fun get`, `operator fun set`, simulation                       |
