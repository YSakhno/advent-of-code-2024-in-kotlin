/*
 * --- Day 9: Disk Fragmenter ---
 *
 * The input is a disk map represented as a string of digits.  The digits alternate between the length of a file and the
 * length of free space.  For example:
 *
 *     2333133121414131402
 *
 * The disk map describes file blocks and free space blocks:
 *
 * - A disk map like 12345 represents a one-block file, two blocks of free space, a three-block file, four blocks of
 * free space, and a five-block file.
 * - Files are assigned Ids in order of appearance starting from 0.  For 12345, the Ids are 0, 1, and 2.
 *
 * The compacting process moves file blocks one at a time starting from the rightmost unmoved block to fill the leftmost
 * free space.  For example:
 *
 * Initial state of 12345 (here and below, the dot (.) represents empty space):
 *
 *     0..111....22222
 *
 * After compaction:
 *
 *     0..111....22222
 *     02.111....2222.
 *     022111....222..
 *     0221112...22...
 *     02211122..2....
 *     022111222......
 *
 * For 2333133121414131402 (the original example) the starting disk state is:
 *
 *     00...111...2...333.44.5555.6666.777.888899
 *
 * After compaction the disk state becomes:
 *
 *     0099811188827773336446555566..............
 *
 * The final checksum is calculated by summing the product of each block's position with its file Id.  Free space blocks
 * are skipped.  Continuing with the first example, the first few blocks' position multiplied by its file Id number are
 * 0 * 0 = 0, 1 * 0 = 0, 2 * 9 = 18, 3 * 9 = 27, 4 * 8 = 32, and so on.  In this example, the checksum is the sum of
 * these, 1928.
 *
 * Compact the disk as described and compute the resulting filesystem checksum.
 *
 * --- Part Two ---
 *
 * This time, attempt to move whole files to the leftmost span of free space blocks that could fit the file.  Attempt to
 * move each file exactly once in order of decreasing file Id number starting with the file with the highest file Id
 * number.  If there is no span of free space to the left of a file that is large enough to fit the file, the file is
 * not moved.
 *
 * The first example from above now yields different files arrangement on disk:
 *
 *     00992111777.44.333....5555.6666.....8888..
 *
 * The process of updating the filesystem checksum is the same; now, this example's checksum would be 2858.
 *
 * What is the resulting filesystem checksum after compacting the initial state using the new method?
 */
package io.ysakhno.adventofcode2024.day09

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private fun String.normalized() = if (length and 1 == 0) this else this + "0"

private fun String.decodeSequence() = normalized().zipWithNext()
    .filterIndexed { idx, _ -> idx % 2 == 0 }
    .map { (file, empty) -> Pair(file.digitToInt(), empty.digitToInt()) }

private fun List<Pair<Int, Int>>.toDisk() = mapIndexed { fileId, (fileLen, emptyLen) ->
    List(fileLen) { fileId } + List(emptyLen) { -1 }
}.flatten().toIntArray()

private val IntArray.checksum
    get() = foldIndexed(0L) { pos, acc, fileId -> if (fileId != -1) acc + pos * fileId else acc }

private fun part1(input: String): Long {
    val disk = input.decodeSequence().toDisk()

    var beginIdx = 0
    var endIdx = disk.lastIndex

    while (true) {
        while (beginIdx < endIdx && disk[beginIdx] != -1) ++beginIdx
        while (beginIdx < endIdx && disk[endIdx] == -1) --endIdx
        if (beginIdx >= endIdx) break
        disk[beginIdx] = disk[endIdx]
        disk[endIdx] = -1
    }

    return disk.checksum
}

private fun part2(input: String): Long {
    val decodedSequence = input.decodeSequence()
    val diskMap = decodedSequence.fold(listOf(Pair(0, 0))) { acc, (fileLen, emptyLen) ->
        acc + Pair(acc.last().let { it.first + it.second } + fileLen, emptyLen)
    }.filter { it.second != 0 }.toMutableList()
    val disk = decodedSequence.toDisk()

    var endIdx = disk.size

    while (endIdx > 0) {
        while (endIdx > 0 && disk[endIdx - 1] == -1) --endIdx
        val thisFileId = disk[endIdx - 1]
        var startIdx = endIdx - 1
        while (startIdx > 0 && disk[startIdx - 1] == thisFileId) --startIdx
        val fileLen = endIdx - startIdx

        val suitableFreeSpaceIdx =
            diskMap.indexOfFirst { (emptyPos, emptyLen) -> emptyPos < startIdx && emptyLen >= fileLen }

        if (suitableFreeSpaceIdx != -1) {
            val freeSpace = diskMap[suitableFreeSpaceIdx]

            repeat(fileLen) { i ->
                disk[freeSpace.first + i] = disk[startIdx + i]
                disk[startIdx + i]  = -1
            }
            diskMap[suitableFreeSpaceIdx] = Pair(freeSpace.first + fileLen, freeSpace.second - fileLen)
        }

        endIdx = startIdx
    }

    return disk.checksum
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest().joinToString(separator = "")
    assertEquals(1928L, part1(testInput), "Part one (sample input)")
    assertEquals(2858L, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read().joinToString(separator = "")
    part1(input).println()
    part2(input).println()
}
