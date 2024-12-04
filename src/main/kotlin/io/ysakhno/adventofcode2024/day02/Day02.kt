/*
 * --- Day 2: Red-Nosed Reports ---
 *
 * You are given a text file containing multiple lines.  Each line represents a report, consisting of space-separated
 * numbers called levels.  For example:
 *
 *     7 6 4 2 1
 *     1 2 7 8 9
 *     9 7 6 2 1
 *     1 3 2 4 5
 *     8 6 4 4 1
 *     1 3 6 7 9
 *
 * A report is considered safe if both of the following conditions hold:
 *
 * - The levels are either all increasing or all decreasing.
 * - Any two adjacent levels differ by at least 1 and at most 3.
 *
 * In the example above, the reports can be found safe or unsafe by checking those rules:
 *
 * - 7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2.
 * - 1 2 7 8 9: Unsafe because 2 7 is an increase of 5.
 * - 9 7 6 2 1: Unsafe because 6 2 is a decrease of 4.
 * - 1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing.
 * - 8 6 4 4 1: Unsafe because 4 4 is neither an increase nor a decrease.
 * - 1 3 6 7 9: Safe because the levels are all increasing by 1, 2, or 3.
 *
 * So, in this example, 2 reports are safe.
 *
 * Count and output the number of safe reports.
 *
 * --- Part Two ---
 *
 * Now, the Problem Dampener makes the same rules apply as before, except if removing a single level from an unsafe
 * report would make it safe, the report instead counts as safe.
 *
 * More of the above example's reports are now safe:
 *
 * - 7 6 4 2 1: Safe without removing any level.
 * - 1 2 7 8 9: Unsafe regardless of which level is removed.
 * - 9 7 6 2 1: Unsafe regardless of which level is removed.
 * - 1 3 2 4 5: Safe by removing the second level, 3.
 * - 8 6 4 4 1: Safe by removing the third level, 4.
 * - 1 3 6 7 9: Safe without removing any level.
 *
 * Thanks to the Problem Dampener, 4 reports are actually safe!
 *
 * Determine how many reports are safe according to the updated rules.
 */
package io.ysakhno.adventofcode2024.day02

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.allInts
import io.ysakhno.adventofcode2024.util.println
import kotlin.math.abs
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val List<Int>.isSafe: Boolean
    get() {
        val pairs = zipWithNext()

        val isIncreasing = pairs.all { (a, b) -> a < b }
        val isDecreasing = pairs.all { (a, b) -> a > b }
        val isWithinTolerance = pairs.all { (a, b) -> abs(a - b) in 1..3 }

        return (isIncreasing || isDecreasing) && isWithinTolerance
    }

private fun List<Int>.removedAt(index: Int) = subList(0, index) + subList(index + 1, size)

private val List<Int>.isSafeWithDampener get() = isSafe || indices.any { removedAt(it).isSafe }

private fun part1(input: List<String>) = input.map(String::allInts).map { it.toList() }.count { it.isSafe }

private fun part2(input: List<String>) = input.map(String::allInts).map { it.toList() }.count { it.isSafeWithDampener }

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(2, part1(testInput), "Part one (sample input)")
    assertEquals(4, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
