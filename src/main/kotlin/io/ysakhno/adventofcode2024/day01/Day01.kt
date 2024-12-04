/*
 * --- Day 1: Historian Hysteria ---
 *
 * Given two lists of integers, pair the smallest number in the first list with the smallest number in the second list,
 * the second smallest with the second smallest, and so on.  Calculate the absolute difference between each pair and sum
 * these differences to obtain the total distance.
 *
 * The input consists of two columns of integers, separated by spaces, one pair per line.
 *
 * For example:
 *
 *     3   4
 *     4   3
 *     2   5
 *     1   3
 *     3   9
 *     3   3
 *
 * In the example list above, the pairs and distances would be as follows:
 *
 * - The smallest number in the left list is 1, and the smallest number in the right list is 3.  The distance between
 *   them is 2.
 * - The second-smallest number in the left list is 2, and the second-smallest number in the right list is another 3.
 *   The distance between them is 1.
 * - The third-smallest number in both lists is 3, so the distance between them is 0.
 * - The next numbers to pair up are 3 and 4, a distance of 1.
 * - The fifth-smallest numbers in each list are 3 and 5, a distance of 2.
 * - Finally, the largest number in the left list is 4, while the largest number in the right list is 9; these are a
 *   distance 5 apart.
 *
 * To find the total distance between the left list and the right list, add up the distances between all of the pairs
 * you found.  In the example above, this is 2 + 1 + 0 + 1 + 2 + 5, a total distance of 11!
 *
 * Calculate and output the total distance, which is the sum of absolute differences between each paired number.
 *
 * --- Part Two ---
 *
 * Calculate a similarity score as follows:
 *
 * 1. For each number in the left list (first column), count how many times it appears in the right list (second
 *    column).
 * 2. Multiply the number by its frequency in the right list and add the results to obtain the total similarity score.
 *
 * For the same example lists as in part one, here is the process of finding the similarity score:
 *
 * - The first number in the left list is 3.  It appears in the right list three times, so the similarity score
 *   increases by 3 * 3 = 9.
 * - The second number in the left list is 4.  It appears in the right list once, so the similarity score increases
 *   by 4 * 1 = 4.
 * - The third number in the left list is 2.  It does not appear in the right list, so the similarity score does not
 *   increase (2 * 0 = 0).
 * - The fourth number, 1, also does not appear in the right list.
 * - The fifth number, 3, appears in the right list three times; the similarity score increases by 9.
 * - The last number, 3, appears in the right list three times; the similarity score again increases by 9.
 *
 * So, for these example lists, the similarity score at the end of this process is 31 (9 + 4 + 0 + 0 + 9 + 9).
 */
package io.ysakhno.adventofcode2024.day01

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.allInts
import io.ysakhno.adventofcode2024.util.println
import io.ysakhno.adventofcode2024.util.transpose
import kotlin.math.abs
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun part1(input: List<String>): Int {
    val (left, right) = input.map(String::allInts).map { it.toList() }.transpose().map(List<Int>::sorted)
    return left.zip(right).map { (l, r) -> abs(l - r) }.sum()
}

private fun part2(input: List<String>): Int {
    val (left, right) = input.map(String::allInts).map { it.toList() }.transpose()
    val counts = right.groupingBy { it }.eachCount()
    return left.sumOf { it * counts.getOrDefault(it, 0) }
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(11, part1(testInput), "Part one (sample input)")
    assertEquals(31, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
