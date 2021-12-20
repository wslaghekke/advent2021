@file:Suppress("KotlinConstantConditions")

import java.io.File

fun main() {
    val actions = File("input/day2.txt").readLines().map {
        val (action, amount) = it.split(" ")
        action to amount.toInt()
    }

    calculatePartOne(actions)
    calculatePartTwo(actions)
}

private fun calculatePartOne(actions: List<Pair<String, Int>>) {
    var horizontal = 0
    var depth = 0

    for ((action, amount) in actions) {
        when (action) {
            "forward" -> horizontal += amount
            "down" -> depth += amount
            "up" -> depth -= amount
        }
    }

    println("Part 1 position: $horizontal, depth: $depth, multiplied: ${horizontal * depth}")
}

private fun calculatePartTwo(actions: List<Pair<String, Int>>) {
    var horizontal = 0
    var depth = 0
    var aim = 0

    for ((action, amount) in actions) {
        when (action) {
            "forward" -> {
                horizontal += amount
                depth += aim * amount
            }
            "down" -> aim += amount
            "up" -> aim -= amount
        }
    }

    println("Part 2 position: $horizontal, depth: $depth, aim: $aim, multiplied: ${horizontal * depth}")
}
