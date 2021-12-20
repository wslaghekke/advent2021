import java.io.File

fun main() {
    val matrix = File("input/day11.txt").readLines()
        .map { line -> line.map { it.toString().toInt() }.toIntArray() }
        .toTypedArray()

    printState(matrix, "Initial state:")

    var flashCount = 0

    repeat(100) {
        flashCount += simulateStep(matrix)
        printState(matrix, "After step ${it + 1}")
    }

    println("Part 1, Flashed $flashCount times")

    var stepCount = 100
    do {
        stepCount++
        val stepFlashCount = simulateStep(matrix)
        printState(matrix, "After step $stepCount")
    } while (stepFlashCount < 100)

    println("Part 2, Simultanious flash after $stepCount steps")
}

private fun printState(matrix: Array<IntArray>, message: String) {
    println(message)
    matrix.forEach {
        println(it.joinToString(""))
    }
    println()
}

private fun simulateStep(matrix: Array<IntArray>): Int {
    val flashedOctopusses = mutableSetOf<Pair<Int, Int>>()

    // Increase energy of every octopus by 1
    for (rowIndex in matrix.indices) {
        for (colIndex in matrix[rowIndex].indices) {
            matrix[rowIndex][colIndex]++
        }
    }

    // Loop until no flashes occur anymore
    do {
        var flashed = false

        // Every octopus with energy level > 9 flashes
        for (rowIndex in matrix.indices) {
            for (colIndex in matrix[rowIndex].indices) {
                if (matrix[rowIndex][colIndex] > 9 && !flashedOctopusses.contains(rowIndex to colIndex)) {
                    // FLASH
                    flashed = true
                    flashedOctopusses.add(rowIndex to colIndex)
                    neighbourSequence(matrix, rowIndex, colIndex).forEach { matrix[it]++ }
                }
            }
        }
    } while (flashed)

    // Set the energy of every octopus that flashed to 0
    flashedOctopusses.forEach { matrix[it] = 0 }

    return flashedOctopusses.size
}

private operator fun Array<IntArray>.get(index: Pair<Int, Int>): Int {
    return this[index.first][index.second]
}

private operator fun Array<IntArray>.set(index: Pair<Int, Int>, value: Int) {
    this[index.first][index.second] = value
}

private fun neighbourSequence(matrix: Array<IntArray>, row: Int, col: Int) = sequence<Pair<Int, Int>> {
    if (row > 0) {
        // Top-left
        if (col > 0) {
            yield(row - 1 to col - 1)
        }

        // Above neighbour
        yield(row - 1 to col)

        // Top-right
        if (col < matrix[0].size - 1) {
            yield(row - 1 to col + 1)
        }
    }

    if (col > 0) {
        // Left neighbour
        yield(row to col - 1)
    }

    if (col < matrix[0].size - 1) {
        // Right neighbour
        yield(row to col + 1)
    }

    if (row < matrix.size - 1) {
        // Bottom-left
        if (col > 0) {
            yield(row + 1 to col - 1)
        }

        // Below neighbour
        yield(row + 1 to col)

        // Bottom-right
        if (col < matrix[0].size - 1) {
            yield(row + 1 to col + 1)
        }
    }

}
