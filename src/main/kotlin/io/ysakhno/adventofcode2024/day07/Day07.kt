/*
 * --- Day 7: Bridge Repair ---
 *
 * Given a list of equations described in the following format:
 *
 *     <Test Value>: <Number1> <Number2> ... <NumberN>
 *
 * Each line contains:
 *
 * - A test value before the colon (:).
 * - A list of numbers separated by spaces after the colon.
 *
 * Determine which equations can be made true by inserting the operators + (addition) or * (multiplication) between the
 * numbers.
 *
 * - Operators are always evaluated left-to-right (not according to standard precedence rules).
 * - Numbers in the equations must remain in the given order.
 *
 * The output is the sum of the test values of all equations that can be made true.
 *
 * Example:
 *
 *     190: 10 19
 *     3267: 81 40 27
 *     83: 17 5
 *     156: 15 6
 *     7290: 6 8 6 15
 *     161011: 16 10 13
 *     192: 17 8 14
 *     21037: 9 7 18 13
 *     292: 11 6 16 20
 *
 * Only the following 3 equations can be made true:
 *
 * - 190: 10 19 is true: 10 * 19 = 190.
 * - 3267: 81 40 27 is true: 81 + 40 * 27 = 3267 or 81 * 40 + 27 = 3267.
 * - 292: 11 6 16 20 is true: 11 + 6 * 16 + 20 = 292.
 *
 * The total calibration result for the valid equations in the example is 190 + 3267 + 292 = 3749.
 *
 * --- Part Two ---
 *
 * Now determine which equations can be made true when a third operator can be used: concatenation (||).  This operator
 * can be used in any combination with the operators from part one.
 *
 * After this new operator is introduced, three more equations from the previous example can be made true (in addition
 * to the three equations that could be made true by using addition and multiplication alone):
 *
 * - 156: 15 6 can be made true through a single concatenation: 15 || 6 = 156.
 * - 7290: 6 8 6 15 can be made true using 6 * 8 || 6 * 15.
 * - 192: 17 8 14 can be made true using 17 || 8 + 14.
 *
 * Adding up all six test values produces the new total calibration result of 11387.
 */
package io.ysakhno.adventofcode2024.day07

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.allLongs
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun inc(digits: IntArray, numDifferentOperators: Int): Boolean {
    var idx = 0

    while (idx < digits.size) {
        if (++digits[idx] >= numDifferentOperators) digits[idx++] = 0 else break
    }

    return idx < digits.size
}

private fun isPossible(numbers: List<Long>, expected: Long, numDifferentOperators: Int): Boolean {
    val operators = IntArray(numbers.size - 1) { 0 }

    do {
        val result = numbers.reduceIndexed { idx, acc, number ->
            when (operators[idx - 1]) {
                0 -> acc * number
                1 -> acc + number
                2 -> (acc.toString() + number.toString()).toLong()
                else -> error("wrong operator code: ${operators[idx - 1]}")
            }
        }
        if (result == expected) return true
    } while (inc(operators, numDifferentOperators))

    return false
}

private fun solve(input: List<String>, partNumber: Int) = input.map { line ->
    val expectedResult = line.substringBefore(':').trim().toLong()
    val numbers = line.substringAfter(':').allLongs().toList()
    numbers to expectedResult
}.filter { isPossible(it.first, it.second, partNumber + 1) }.sumOf { it.second }

private fun part1(input: List<String>) = solve(input, 1)

private fun part2(input: List<String>) = solve(input, 2)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(3749L, part1(testInput), "Part one (sample input)")
    assertEquals(11387L, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
