import java.io.File

fun main() {
    val matrix = File("input/day9.txt").readLines()
        .map { line -> line.map { it.toString().toInt() }.toIntArray() }
        .toTypedArray()


    val totalLowPointRisk = matrix.foldIndexed(0) { rowIndex, totalRisk, row ->
        totalRisk + row.foldIndexed(0) { colIndex, rowRisk, point ->
            if (isLowPoint(matrix, rowIndex, colIndex)) {
                rowRisk + point + 1
            } else {
                rowRisk
            }
        }
    }

    println("Part 1, total risk: $totalLowPointRisk")

    val largestBasinsProduct = matrixSequence(matrix)
        .filter { (row, col) -> isLowPoint(matrix, row, col) }
        .map {
            val basinPoints = mutableSetOf<Pair<Int, Int>>()
            addPointToBasin(matrix, it, basinPoints)
            println("Found basin of size ${basinPoints.size}")

            basinPoints.size
        }
        .sortedDescending()
        .take(3)
        .reduce { a, b -> a * b }

    println("Part 2, three largest basins multiplied: $largestBasinsProduct")
}

fun addPointToBasin(
    matrix: Array<IntArray>,
    point: Pair<Int, Int>,
    basinPoints: MutableSet<Pair<Int, Int>>
) {
    basinPoints.add(point)
    neighbourPoints(matrix, point.first, point.second).forEach {
        // Skip if point already part of basin and height 9 is excluded from basins
        if (!basinPoints.contains(it) && matrix[it.first][it.second] < 9) {
            addPointToBasin(matrix, it, basinPoints)
        }
    }

}

private fun matrixSequence(matrix: Array<IntArray>) = sequence<Pair<Int, Int>> {
    matrix.forEachIndexed { rowIndex, row ->
        for (colIndex in row.indices) {
            yield(rowIndex to colIndex)
        }
    }
}

private fun isLowPoint(matrix: Array<IntArray>, row: Int, col: Int): Boolean {
    val current = matrix[row][col]
    return neighbourPoints(matrix, row, col).all { (row, col) -> matrix[row][col] > current }
}

private fun neighbourPoints(matrix: Array<IntArray>, row: Int, col: Int) = sequence<Pair<Int, Int>> {
    if (row > 0) {
        // Above neighbour
        yield(row - 1 to col)
    }
    if (col > 0) {
        // Left neighbour
        yield(row to col - 1)
    }
    if (row < matrix.size - 1) {
        // Below neighbour
        yield(row + 1 to col)
    }
    if (col < matrix[0].size - 1) {
        // Right neighbour
        yield(row to col + 1)
    }
}
