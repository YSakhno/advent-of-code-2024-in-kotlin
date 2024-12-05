/*
 * --- Day 5: Print Queue ---
 *
 * You are given a list of page ordering rules and updates.  The task is to determine, which updates follow the given
 * rules and calculate the sum of the middle page numbers for these correctly-ordered updates.
 *
 * The input consists of two sections separated by a blank line:
 *
 * - Ordering Rules: Each line contains a rule in the format X|Y, meaning if both page number X and page number Y are
 *   present in an update, X must come before Y.
 * - Updates: Each line contains a comma-separated list of page numbers representing an update.
 *
 * Example Input:
 *     47|53
 *     97|13
 *     97|61
 *     97|47
 *     75|29
 *     61|13
 *     75|53
 *     29|13
 *     97|29
 *     53|29
 *     61|53
 *     97|53
 *     61|29
 *     47|13
 *     75|47
 *     97|75
 *     47|61
 *     75|61
 *     47|29
 *     75|13
 *     53|13
 *
 *     75,47,61,53,29
 *     97,61,53,29,13
 *     75,29,13
 *     75,97,47,61,53
 *     61,13,29
 *     97,13,75,29,47
 *
 * In the example above, the first, second and third updates (75,47,61,53,29; 97,61,53,29,13; 75,29,13 respectively) are
 * in the correct order according to the rules.
 *
 * The fourth update, 75,97,47,61,53, is not in the correct order: it has 75 before 97, which violates the rule 97|75.
 *
 * The fifth update, 61,13,29, is also not in the correct order, since it breaks the rule 29|13.
 *
 * The last update, 97,13,75,29,47, is not in the correct order due to breaking several rules.
 *
 * Determine, which updates are in the correct order according to the rules.  For each correctly-ordered update, find
 * its middle page number.  Sum these middle page numbers and output the total as a single number.
 *
 * --- Part Two ---
 *
 * Now, for each of the incorrectly-ordered updates, use the page ordering rules to put the page numbers in the right
 * order.  For the above example, here are the three incorrectly-ordered updates and their correct orderings:
 *
 * - 75,97,47,61,53 becomes 97,75,47,61,53.
 * - 61,13,29 becomes 61,29,13.
 * - 97,13,75,29,47 becomes 97,75,47,29,13.
 *
 * Their corresponding middle page numbers are 47, 29, and 47.  Adding these together produces 123.
 *
 * What is the sum of the middle page numbers of the reordered updates?
 */
package io.ysakhno.adventofcode2024.day05

import io.ysakhno.adventofcode2024.util.ProblemInput
import io.ysakhno.adventofcode2024.util.allInts
import io.ysakhno.adventofcode2024.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private typealias Ordering = Pair<Int, Int>
private typealias Update = List<Int>

private fun getOrderingsAndUpdates(input: List<String>): Pair<Set<Ordering>, List<Update>> {
    val (left, right) = input.partition { it.contains('|') }
    val orderings = left.map(String::allInts).map(Sequence<Int>::toList).map { (p1, p2) -> p1 to p2 }.toSet()
    val updates = right.map(String::allInts).map(Sequence<Int>::toList)

    return orderings to updates
}

private fun Update.isInCorrectOrder(orderings: Set<Ordering>): Boolean {
    for (i in 0..<lastIndex) {
        for (j in i+1..lastIndex) {
            if (Pair(this[j], this[i]) in orderings) return false
        }
    }
    return true
}

private val List<Update>.sumOfMiddlePages get() = sumOf { it[it.size / 2] }

private fun part1(input: List<String>): Int {
    val (orderings, updates) = getOrderingsAndUpdates(input)
    return updates.filter { it.isInCorrectOrder(orderings) }.sumOfMiddlePages
}

private fun MutableList<Int>.fixedBy(orderings: Set<Ordering>): Update {
    outer@while (!isInCorrectOrder(orderings)) {
        for (i in 0..<lastIndex) {
            for (j in i+1..lastIndex) {
                if (Pair(this[j], this[i]) in orderings) {
                    val tmp = this[j]
                    this[j] = this[i]
                    this[i] = tmp
                    continue@outer
                }
            }
        }
    }
    return toList()
}

private fun part2(input: List<String>): Int {
    val (orderings, updates) = getOrderingsAndUpdates(input)
    return updates.filter { !it.isInCorrectOrder(orderings) }
        .map(Update::toMutableList)
        .map { it.fixedBy(orderings) }
        .sumOfMiddlePages
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(143, part1(testInput), "Part one (sample input)")
    assertEquals(123, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
