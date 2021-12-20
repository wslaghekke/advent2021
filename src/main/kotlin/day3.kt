@file:Suppress("KotlinConstantConditions")

import java.io.File

fun main() {
    val lines = File("input/day3.txt").readLines()

    val oneMostCommon = lines
        .map { it.toList() }
        .fold(Array(lines.first().length) { 0 }) { acc, bits ->
            bits.forEachIndexed { index, char -> if (char == '1') acc[index]++; }
            acc
        }
        .map { it > (lines.size / 2) }


    calculatePartOne(oneMostCommon)
    calculatePartTwo(lines)
}

private fun calculatePartOne(oneMostCommon: List<Boolean>) {
    val gamma = Integer.parseInt(oneMostCommon.joinToString("") { if (it) "1" else "0" }, 2)
    val epsilon = Integer.parseInt(oneMostCommon.joinToString("") { if (it) "0" else "1" }, 2)

    println("Part 1 gamma: $gamma, epsilon: $epsilon, result: ${gamma * epsilon}")
}

private fun calculatePartTwo(lines: List<String>) {
    val oxygenRating = calculateRating(lines, { a, b, bitIndex ->
        if (a.size > b.size || (a.size == b.size && a[0][bitIndex] == '1')) a else b
    })
    val co2Rating = calculateRating(lines, { a, b, bitIndex ->
        if (a.size < b.size || (a.size == b.size && a[0][bitIndex] == '0')) a else b
    })

    println("Part 2 oxygen: $oxygenRating, co2: $co2Rating, result: ${oxygenRating * co2Rating}")
}

private fun calculateRating(
    lines: List<String>,
    groupCompareFn: (a: List<String>, b: List<String>, bitIndex: Int) -> List<String>,
    bitIndex: Int = 0,
): Int {
    val filteredLines = lines
        .groupBy { it[bitIndex] }
        .values
        .reduce { prev, curr -> groupCompareFn(prev, curr, bitIndex) }

    return if (filteredLines.size == 1) {
        Integer.parseInt(filteredLines[0], 2)
    } else {
        calculateRating(filteredLines, groupCompareFn, bitIndex + 1)
    }
}