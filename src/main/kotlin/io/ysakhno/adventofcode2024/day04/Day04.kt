/*
 * --- Day 4: Ceres Search ---
 *
 * Given a word search in a text file, find all occurrences of the word XMAS.  Words can appear horizontally,
 * vertically, diagonally, backwards, or overlapping other words.
 *
 * Example Input:
 *
 *     MMMSXXMASM
 *     MSAMXMSMSA
 *     AMXSXMAAMM
 *     MSAMASMSMX
 *     XMASAMXAMM
 *     XXAMMXXAMA
 *     SMSMSASXSS
 *     SAXAMASAAA
 *     MAMMMXMMMM
 *     MXMXAXMASX
 *
 * Explanation of the example: The word XMAS appears a total of 18 times in the example input.
 *
 * --- Part Two ---
 *
 * Given a grid of uppercase letters representing a word search, find how many times an X-MAS pattern appears.  The
 * X-MAS pattern consists of two occurrences of MAS written diagonally and forming an "X" shape:
 *
 *     M.S
 *     .A.
 *     M.S
 *
 * Within the X, each MAS can be written forwards or backwards.  Irrelevant characters are ignored and replaced with
 * dots (.) in the examples.
 *
 * Here's the same example from before, but this time all of the X-MASes have been kept instead:
 *
 *     .M.S......
 *     ..A..MSMS.
 *     .M.S.MAA..
 *     ..A.ASMSM.
 *     .M.S.M....
 *     ..........
 *     S.S.S.S.S.
 *     .A.A.A.A..
 *     M.M.M.M.M.
 *     ..........
 *
 * In this example, an X-MAS appears 9 times.
 *
 * Count all valid X-MAS patterns in the given input grid and return the total number.
 */
package io.ysakhno.adventofcode2024.day04

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import io.ysakhno.adventofcode2024.util.transpose
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val XMAS_REGEX = "XMAS".toRegex()

private val List<List<Char>>.xmasCount
    get() = map { it.joinToString(separator = "") }.sumOf { XMAS_REGEX.findAll(it).count() }

private fun List<List<Char>>.skewed() = mapIndexed { idx, chars ->
    ".".repeat(chars.size - idx - 1).toList() + chars + ".".repeat(idx).toList()
}

private fun part1(input: List<String>): Int {
    val grid1 = input.map { it.map { ch -> if (ch in "XMAS") ch else '.' } } // left-to-right
    val grid2 = grid1.map { it.reversed() } // right-to-left
    val grid3 = grid1.transpose() // top-to-bottom
    val grid4 = grid1.reversed().transpose() // bottom-to-top
    val grid5 = grid1.skewed().transpose() // diagonal upper-left-to-lower-right
    val grid6 = grid2.skewed().transpose() // diagonal upper-right-to-lower-left
    val grid7 = grid1.reversed().skewed().transpose() // diagonal lower-left-to-upper-right
    val grid8 = grid2.reversed().skewed().transpose() // diagonal lower-right-to-upper-left

    return grid1.xmasCount + grid2.xmasCount + grid3.xmasCount + grid4.xmasCount +
            grid5.xmasCount + grid6.xmasCount + grid7.xmasCount + grid8.xmasCount
}

private fun List<List<Char>>.subBlock(x: Int, y: Int): List<List<Char>> {
    require(y >= 0 && y + 3 <= size) { "Invalid y coordinate" }
    require(x >= 0 && x + 3 <= first().size) { "Invalid x coordinate" }

    return (y..<y + 3).map(this::get).map { it.subList(x, x + 3) }
}

private val xmas1 = listOf(
    "M?M",
    "?A?",
    "S?S",
)
private val xmas2 = listOf(
    "M?S",
    "?A?",
    "M?S",
)
private val xmas3 = listOf(
    "S?M",
    "?A?",
    "S?M",
)
private val xmas4 = listOf(
    "S?S",
    "?A?",
    "M?M",
)

private val xmasPatterns = listOf(xmas1, xmas2, xmas3, xmas4)

private fun List<List<Char>>.matchesPattern(pattern: List<String>): Boolean {
    if (size != pattern.size) return false
    if (isEmpty()) return true
    if (first().size != pattern.first().length) return false

    for (i in indices) {
        for (j in first().indices) {
            val ch = pattern[i][j]
            if (ch != '?' && ch != this[i][j]) return false
        }
    }

    return true
}

private fun List<List<Char>>.matchesPattern(): Boolean = xmasPatterns.any(::matchesPattern)

private fun part2(input: List<String>): Int {
    val grid = input.map { it.map { ch -> if (ch in "MAS") ch else '.' } }
    var count = 0

    for (y in 0..grid.size-3) {
        for (x in 0..grid.first().size-3) {
            if (grid.subBlock(x, y).matchesPattern()) ++count
        }
    }

    return count
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(18, part1(testInput), "Part one (sample input)")
    assertEquals(9, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
