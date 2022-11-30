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

| Problem | Solution | Blog Post | Tags |
|---------|----------|-----------|------|
