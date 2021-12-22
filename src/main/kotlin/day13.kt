import java.io.File
import kotlin.math.max

fun main() {
    val foldRegex = Regex("fold along ([xy])=(\\d+)")
    val requestedFolds = mutableListOf<Fold>()
    val dotCoordinates = mutableListOf<Pair<Int, Int>>()
    var largestX = 0
    var largestY = 0

    var endOfCoordinatesReached = false
    for (line in File("input/day13.txt").bufferedReader().lines()) {
        if (line == "") {
            endOfCoordinatesReached = true
        } else if (!endOfCoordinatesReached) {
            val (x, y) = line.split(',')
            largestX = max(largestX, x.toInt())
            largestY = max(largestY, y.toInt())
            dotCoordinates.add(x.toInt() to y.toInt())
        } else {
            foldRegex.matchEntire(line)?.destructured?.let { (axis, amount) ->
                requestedFolds.add(
                    Fold(
                        if (axis == "x") FoldAxis.X else FoldAxis.Y,
                        amount.toInt()
                    )
                )
            } ?: throw Exception("Invalid line '$line'")
        }
    }

    var matrix = Array(largestY + 1) { BooleanArray(largestX + 1) { false } }

    dotCoordinates.forEach { (x, y) ->
        matrix[y][x] = true
    }

    println("Initial matrix")
    printMatrix(matrix)


    for (fold in requestedFolds) {
        println()
        matrix = foldMatrix(matrix, fold)
        printMatrix(matrix)
        println("${countMatrixDots(matrix)} visible dots in matrix")
    }




    println()
    println("Part 1")

    println()
    println("Part 2")
}

private fun printMatrix(matrix: Array<BooleanArray>) {
    val dotWidth = matrix[0].lastIndex.toString().length

    println(matrix[0].indices.joinToString(" ") { it.toString().padEnd(dotWidth, ' ') })
    matrix.forEachIndexed { index, line ->
        println(line.joinToString(" ") { (if (it) "█" else "░").repeat(dotWidth) } + " $index")
    }
}

private fun countMatrixDots(matrix: Array<BooleanArray>): Int {
    return matrix.sumOf { row -> row.count { it } }
}

private fun foldMatrix(matrix: Array<BooleanArray>, fold: Fold): Array<BooleanArray> {
    return when(fold.axis) {
        FoldAxis.Y -> {
            val newMatrix = Array(fold.index) { matrix[it] }
            // Loop over dots after fold
            for (y in (fold.index + 1) until matrix.size) {
                val foldedY = y - ((y - fold.index) * 2)
                matrix[foldedY].mergeOr(matrix[y])
            }

            newMatrix
        }
        FoldAxis.X -> {
            Array(matrix.size) { y ->
                BooleanArray(fold.index) { x -> matrix[y][x] }.also {
                    // Loop over dots after fold
                    for(x in (fold.index + 1) until matrix[y].size) {
                        val foldedX = x - ((x - fold.index) * 2)
                        it[foldedX] = it[foldedX] || matrix[y][x]
                    }
                }
            }
        }
    }
}

private enum class FoldAxis { X, Y }
private data class Fold(val axis: FoldAxis, val index: Int)

private fun BooleanArray.mergeOr(other: BooleanArray) {
    require(this.size == other.size) { "Size of arrays must be equal" }

    for (index in this.indices) {
        this[index] = this[index] || other[index]
    }
}
