/*
 * --- Day 8: Resonant Collinearity ---
 *
 * You are given a rectangular grid map containing antennas.  Each antenna is represented by a single lowercase letter,
 * uppercase letter, or digit, and empty spaces are denoted by a dot (.).  For example:
 *
 *     ............
 *     ........0...
 *     .....0......
 *     .......0....
 *     ....0.......
 *     ......A.....
 *     ............
 *     ............
 *     ........A...
 *     .........A..
 *     ............
 *     ............
 *
 * An antinode occurs at any point that is perfectly in line with two antennas of the same frequency, where one antenna
 * is twice as far away as the other.  This rule applies to antennas with the same frequency only; antennas with
 * different frequencies do not interact to create antinodes.  For example, given two antennas with frequency "a", the
 * grid may look like this:
 *
 *     ..........
 *     ...#......
 *     ..........
 *     ....a.....
 *     ..........
 *     .....a....
 *     ..........
 *     ......#...
 *     ..........
 *     ..........
 *
 * Adding a third antenna with the same frequency creates additional antinodes.
 *
 * Antinodes occurring outside the map are not counted (ignored).  Antinodes can occur at locations containing antennas.
 *
 * Given a grid of antennas and empty spaces, calculate the number of unique locations within the bounds of the map that
 * contain an antinode.  The first example above produces a total of 14 antinodes (including an antinode that is
 * overlapping the topmost antenna with frequency "A").
 *
 * --- Part Two ---
 *
 * An antinode now occurs at any grid position exactly in line with at least two antennas of the same frequency,
 * regardless of distance.  Each antenna of a frequency is itself considered an antinode unless it is the only one of
 * its frequency in the map.
 *
 * Here is an example of a map with antennas of  frequency "T", and antinodes resulting from such antenna arrangement
 * (locations of antinodes are indicated with "#"):
 *
 *     T....#....
 *     ...T......
 *     .T....#...
 *     .........#
 *     ..#.......
 *     ..........
 *     ...#......
 *     ..........
 *     ....#.....
 *     ..........
 *
 * In this example, the three antennas create a total of 9 antinodes, including the antennas themselves.
 *
 * With the updated rules in mind, the original example map now produces 34 antinodes:
 *
 *     ##....#....#
 *     .#.#....0...
 *     ..#.#0....#.
 *     ..##...0....
 *     ....0....#..
 *     .#...#A....#
 *     ...#..#.....
 *     #....#.#....
 *     ..#.....A...
 *     ....#....A..
 *     .#........#.
 *     ...#......##
 *
 * Calculate the total number of unique locations within the bounds of the map that contain an antinode using the
 * updated rules.
 */
package io.ysakhno.adventofcode2024.day08

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun getAllAntennas(input: List<String>) =
    input.flatMapIndexed { y, line -> line.mapIndexed { x, a -> a to Pair(x, y) } }
        .filterNot { it.first == '.' }
        .groupingBy { it.first }
        .aggregate { _, acc: List<Pair<Int, Int>>?, el, _ -> (acc ?: emptyList()) + el.second }
        .values
        .filter { it.size > 1 }

private fun <E> List<E>.choose2() = buildList {
    for (i in 0..<this@choose2.lastIndex) {
        val element1 = this@choose2[i]
        for (j in i + 1..this@choose2.lastIndex) {
            val element2 = this@choose2[j]
            this += Pair(element1, element2)
        }
    }
}

private fun part1(input: List<String>): Int {
    val fieldWidth = input.first().length
    val fieldHeight = input.size

    return getAllAntennas(input)
        .flatMap { it.choose2() }
        .flatMap { (antenna1, antenna2) ->
            val deltaX = antenna2.first - antenna1.first
            val deltaY = antenna2.second - antenna1.second
            val coordX1 = antenna1.first - deltaX
            val coordY1 = antenna1.second - deltaY
            val coordX2 = antenna2.first + deltaX
            val coordY2 = antenna2.second + deltaY

            buildList {
                if (coordX1 in 0..<fieldWidth && coordY1 in 0..<fieldHeight) this += Pair(coordX1, coordY1)
                if (coordX2 in 0..<fieldWidth && coordY2 in 0..<fieldHeight) this += Pair(coordX2, coordY2)
            }
        }
        .distinct()
        .size
}

private fun part2(input: List<String>): Int {
    val fieldWidth = input.first().length
    val fieldHeight = input.size

    return getAllAntennas(input)
        .flatMap { it.choose2() }
        .flatMap { (antenna1, antenna2) ->
            buildList {
                val deltaX = antenna2.first - antenna1.first
                val deltaY = antenna2.second - antenna1.second
                var coordX = antenna1.first
                var coordY = antenna1.second

                while (coordX in 0..<fieldWidth && coordY in 0..<fieldHeight) {
                    this += Pair(coordX, coordY)
                    coordX -= deltaX
                    coordY -= deltaY
                }

                coordX = antenna2.first
                coordY = antenna2.second

                while (coordX in 0..<fieldWidth && coordY in 0..<fieldHeight) {
                    this += Pair(coordX, coordY)
                    coordX += deltaX
                    coordY += deltaY
                }
            }
        }
        .distinct()
        .size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(14, part1(testInput), "Part one (sample input)")
    assertEquals(34, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
