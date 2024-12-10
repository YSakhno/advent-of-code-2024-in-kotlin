/*
 * --- Day 10: Hoof It ---
 */
package io.ysakhno.adventofcode2024.day10

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private val deltas = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

private fun List<List<Int>>.getPathsAt(startX: Int, startY: Int, isDistinct: Boolean): Int {
    if (this[startY][startX] != 0) return 0

    val mapWidth = first().size
    val mapHeight = size
    val queue = ArrayDeque<Pair<Int, Int>>().apply { add(Pair(startX, startY)) }
    val positions = if (isDistinct) mutableListOf<Pair<Int, Int>>() else mutableSetOf()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val (curX, curY) = current
        val curHeight = this[curY][curX]

        if (curHeight == 9) {
            positions += current
        }

        queue += deltas.map { (dx, dy) -> Pair(curX + dx, curY + dy) }
            .filter { (x) -> x in 0..<mapWidth }
            .filter { (_, y) -> y in 0..<mapHeight }
            .filter { (x, y) -> this[y][x] == curHeight + 1 }
    }

    return positions.size
}

private fun List<List<Int>>.getScoreAt(startX: Int, startY: Int) = getPathsAt(startX, startY, false)

private fun List<List<Int>>.getRatingAt(startX: Int, startY: Int) = getPathsAt(startX, startY, true)

private fun part1(input: List<String>): Int {
    val topoMap = input.map { it.map(Char::digitToInt) }
    return topoMap.flatMapIndexed { y, row -> row.mapIndexed { x, _ -> topoMap.getScoreAt(x, y) } }.sum()
}

private fun part2(input: List<String>): Int {
    val topoMap = input.map { it.map(Char::digitToInt) }
    return topoMap.flatMapIndexed { y, row -> row.mapIndexed { x, _ -> topoMap.getRatingAt(x, y) } }.sum()
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(36, part1(testInput), "Part one (sample input)")
    assertEquals(81, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
