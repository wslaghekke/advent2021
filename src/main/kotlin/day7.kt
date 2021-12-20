import java.io.File
import kotlin.math.abs

fun main() {
    val state = File("input/day7.txt")
        .readLines()
        .flatMap { line -> line.split(',').map { it.toInt() } }
        .toMutableList()

    val minCrabPos = state.minOrNull()!!
    val maxCrabPos = state.maxOrNull()!!

    val cheapestPos = (minCrabPos..maxCrabPos).minOf { calculateCrabMoveCost(state, it) }

    println("Part 1: Optimal cost: $cheapestPos")

    val part2CheapestPos = (minCrabPos..maxCrabPos).minOf { calculateCrabMoveCostPartTwo(state, it) }

    println("Part 2: Optimal cost: $part2CheapestPos")
}

fun calculateCrabMoveCost(crabPositions: List<Int>, targetPosition: Int): Int {
    return crabPositions.sumOf { crabPosition -> abs(crabPosition - targetPosition) }
}

fun calculateCrabMoveCostPartTwo(crabPositions: List<Int>, targetPosition: Int): Int {
    return crabPositions.sumOf { crabPosition ->
        val stepCount = abs(crabPosition - targetPosition)
        var currentStepCost = 1
        var totalCost = 0
        for (i in 1..stepCount) {
            totalCost += currentStepCost
            currentStepCost++
        }

        totalCost
    }
}
