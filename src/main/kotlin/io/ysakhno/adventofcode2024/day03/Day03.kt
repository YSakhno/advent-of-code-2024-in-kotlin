/*
 * --- Day 3: Mull It Over ---
 *
 * Scan the provided input file, which contains a corrupted sequence of characters, for valid multiplication
 * instructions of the form "mul(X,Y)" (quotes shown for clarity only), where:
 *
 * - X and Y are integers with 1 to 3 digits.
 * - Neither X nor Y start with a digit 0 (this means also that neither X nor Y are 0).
 * - Only instructions exactly matching the pattern mul(X,Y) (with no spaces or additional characters) should be
 *   considered valid.
 *
 * For example, in the following input:
 *
 *     xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
 *
 * The valid instructions are:
 *
 * - mul(2,4)
 * - mul(5,5)
 * - mul(11,8)
 * - mul(8,5)
 *
 * These instructions yield the sum: 2*4 + 5*5 + 11*8 + 8*5 = 8 + 25 + 88 + 40 = 161
 *
 * Calculate the results of the valid mul instructions and return their sum.
 *
 * --- Part Two ---
 *
 * There are two new instructions that need to be handled:
 *
 * - The do() instruction enables future mul instructions.
 * - The don't() instruction disables future mul instructions.
 *
 * Only the most recent do() or don't() instruction applies.  At the beginning of the program, mul instructions are
 * enabled.
 *
 * For example:
 *
 * xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
 *
 * This corrupted memory is similar to the example from before, but this time the mul(5,5) and mul(11,8) instructions
 * are disabled because there is a don't() instruction before them.  The other mul instructions function normally,
 * including the one at the end that gets re-enabled by a do() instruction.
 *
 * This time, the sum of the results is 48 (2*4 + 8*5).
 *
 * What is the sum of the results of all valid and enabled multiplications?
 */
package io.ysakhno.adventofcode2024.day03

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val MUL_REGEX = "mul\\((?<first>[0-9]{1,3}),(?<second>[0-9]{1,3})\\)".toRegex()
private val MUL_WITH_CONDITIONS_REGEX =
    """do\(\)|don't\(\)|mul\((?<first>[0-9]{1,3}),(?<second>[0-9]{1,3})\)""".toRegex()

private fun part1(input: List<String>) = input.sumOf { line ->
    MUL_REGEX.findAll(line).sumOf { match ->
        val first = match.groups["first"]!!.value.toInt()
        val second = match.groups["second"]!!.value.toInt()
        first * second
    }
}

private fun part2(input: List<String>): Int {
    var isEnabled = true
    return input.sumOf { line ->
        MUL_WITH_CONDITIONS_REGEX.findAll(line).mapNotNull { match ->
            if (match.value == "do()") {
                isEnabled = true
                null
            } else if (match.value == "don't()") {
                isEnabled = false
                null
            } else if (isEnabled) {
                val first = match.groups["first"]!!.value.toInt()
                val second = match.groups["second"]!!.value.toInt()
                first * second
            } else {
                null
            }
        }.sum()
    }
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(161, part1(testInput), "Part one (sample input)")
    assertEquals(48, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
