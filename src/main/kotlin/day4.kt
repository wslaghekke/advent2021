@file:Suppress("KotlinConstantConditions")

import java.io.File

fun main() {
    val lines = File("input/day4.txt").readLines()

    val numbers = lines.first().split(',').map { it.toInt() }

    val cards = lines.drop(2)
        .windowed(size = 5, step = 6)
        .asSequence()
        .map { cardLines ->
            BingoCard(
                cardLines.flatMap { line ->
                    line
                        .split(" +".toRegex())
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
                }.toMutableList()
            )
        }

    calculatePartOne(numbers, cards.toList())
    calculatePartTwo(numbers, cards.toList())
}

fun calculatePartOne(numbers: List<Int>, cards: List<BingoCard>) {
    val firstWinningCard = winningCardSequence(numbers, cards).first()
    println("Part 1, final score: ${firstWinningCard.calculateScore()}")
}

fun calculatePartTwo(numbers: List<Int>, cards: List<BingoCard>) {
    val lastWinningCard = winningCardSequence(numbers, cards).last()
    println("Part 2, final score: ${lastWinningCard.calculateScore()}")
}

fun winningCardSequence(numbers: List<Int>, cards: List<BingoCard>) = sequence {
    for (number in numbers) {
        cards.forEach { card ->
            card.markNumber(number)
            if (card.hasWon()) {
                yield(card)
            }
        }
    }
}

class BingoCard(
    private val numbers: MutableList<Int>
) {
    private var lastNumber = -1

    private var winCalled = false

    fun markNumber(number: Int) {
        if (winCalled) { return }

        lastNumber = number
        numbers.forEachIndexed { index, cardNumber ->
            if (cardNumber == number) {
                numbers[index] = -1
            }
        }
    }

    fun hasWon(): Boolean {
        if (winCalled) return false

        return sequenceOf(
            // Horizontal
            arrayOf(0, 1, 2, 3, 4),
            arrayOf(5, 6, 7, 8, 9),
            arrayOf(10, 11, 12, 13, 14),
            arrayOf(15, 16, 17, 18, 19),
            arrayOf(20, 21, 22, 23, 24),
            // Vertical
            arrayOf(0, 5, 10, 15, 20),
            arrayOf(1, 6, 11, 16, 21),
            arrayOf(2, 7, 12, 17, 22),
            arrayOf(3, 8, 13, 18, 23),
            arrayOf(4, 9, 14, 19, 24),
            //        // Diagonal
            //        arrayOf(0, 6, 12, 18, 24),
            //        arrayOf(4, 8, 12, 16, 20)
        ).any {
            allMarked(it)
        }.also { won ->
            if (won) {
                winCalled = true
            }
        }
    }

    fun calculateScore(): Int {
        return numbers.filter { it != -1 }.sum() * lastNumber
    }

    private fun allMarked(indexes: Array<Int>): Boolean {
        return indexes.fold(true) { marked, index -> marked && numbers[index] == -1 }
    }
}
