/*
 * --- Day 6: Guard Gallivant ---
 *
 * Given a map of a lab as a puzzle input, determine how many distinct positions a guard will visit before leaving the
 * mapped area, including the starting position.
 *
 * The map has the following format:
 *
 * - The guard's current position is marked with ^ to indicate the guard is initially facing up.
 * - Obstructions (e.g., crates, desks) are marked with #.
 * - Empty spaces are marked with '.' (dot).
 *
 * Guard Movement Rules:
 *
 * - If there is an obstacle directly in front of the guard, they turn right 90 degrees.
 * - Otherwise, the guard takes a step forward.
 *
 * Example of the map:
 *
 *     ....#.....
 *     .........#
 *     ..........
 *     ..#.......
 *     .......#..
 *     ..........
 *     .#..^.....
 *     ........#.
 *     #.........
 *     ......#...
 *
 * By following the movement rules, the guard will visit the following positions (marked with an "X") before leaving the
 * map, 41 visited positions in total (some positions may be visited more than once, but each such position is only
 * counted once):
 *
 *     ....#.....
 *     ....XXXXX#
 *     ....X...X.
 *     ..#.X...X.
 *     ..XXXXX#X.
 *     ..X.X.X.X.
 *     .#XXXXXXX.
 *     .XXXXXXX#.
 *     #XXXXXXX..
 *     ......#X..
 *
 * --- Part Two ---
 *
 * It is possible to place a single new obstacle on the map to cause the guard to perpetually walk in a loop, without
 * ever leaving the map.  The new task is to determine in how many distinct positions a single new obstacle can be
 * placed such that the guard becomes stuck in a loop.  The new obstruction cannot be placed at the guard's starting
 * position.
 *
 * For the map from the example above, there are 6 such positions, marked by an "O" in the map below:
 *
 *     ....#.....
 *     .........#
 *     ..........
 *     ..#.......
 *     .......#..
 *     ..........
 *     .#.O^.....
 *     ......OO#.
 *     #O........
 *     ......#O..
 */
package io.ysakhno.adventofcode2024.day06

import io.ysakhno.adventofcode2024.day06.Direction.DOWN
import io.ysakhno.adventofcode2024.day06.Direction.LEFT
import io.ysakhno.adventofcode2024.day06.Direction.RIGHT
import io.ysakhno.adventofcode2024.day06.Direction.UP
import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private typealias LabMap = List<String>

private enum class Direction { UP, RIGHT, DOWN, LEFT }

private val Direction.next get() = when (this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
}

private data class Position(val x: Int, val y: Int, val dir: Direction)

private val Position.next
    get() = when (dir) {
        UP -> copy(x = x, y = y - 1)
        RIGHT -> copy(x = x + 1, y = y)
        DOWN -> copy(x = x, y = y + 1)
        LEFT -> copy(x = x - 1, y = y)
    }

private val LabMap.positionOfTheGuard: Position
    get() {
        for (y in indices) {
            for (x in this[y].indices) {
                if (this[y][x] == '^') return Position(x, y, UP)
            }
        }
        error("No guard found")
    }

private fun LabMap.isPosLegal(pos: Position) = pos.y in indices && pos.x in 0..<first().length

private fun LabMap.computeGuardsPath(): List<Position> {
    var guard = positionOfTheGuard
    val visited = mutableSetOf(guard)

    while (true) {
        val nextPos = guard.next
        if (nextPos in visited) return emptyList() // loop found
        if (!isPosLegal(nextPos)) break // walked outside the map
        if (this[nextPos.y][nextPos.x] == '#') {
            guard = guard.copy(dir = guard.dir.next)
            continue
        }
        visited += nextPos
        guard = nextPos
    }

    return visited.toList()
}

private fun part1(map: LabMap) = map.computeGuardsPath().distinctBy { it.x to it.y }.size

private fun LabMap.putObstacleAt(x: Int, y: Int) = mapIndexed { rowNum, row ->
    if (rowNum == y) row.mapIndexed { colNum, ch -> if (colNum == x) '#' else ch }.joinToString(separator = "") else row
}

private fun part2(map: LabMap) = map.computeGuardsPath()
    .asSequence()
    .distinctBy { it.x to it.y }
    .filter { map[it.y][it.x] != '^' }
    .map { map.putObstacleAt(it.x, it.y) }
    .map(LabMap::computeGuardsPath)
    .count { it.isEmpty() }

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(41, part1(testInput), "Part one (sample input)")
    assertEquals(6, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
