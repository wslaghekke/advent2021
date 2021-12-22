import java.io.File
import java.math.BigInteger

fun main() {
    val lines = File("input/day10.txt").readLines()

    var totalSyntaxErrorScore = 0
    val autocompleteScores = mutableListOf<BigInteger>()

    for (line in lines) {
        // Cant inline because then kotlin type inference fails
        try {
            validateLine(line)
        } catch (ex: InvalidCharException) {
            println(ex.message)
            totalSyntaxErrorScore += when (ex.actual) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> throw IllegalArgumentException("Unexpected char '${ex.actual}'")
            }
        } catch (ex: IncompleteLineException) {
            println(ex.message)
            val lineAutocompleteScore = ex.autoComplete.fold(0.toBigInteger()) { totalScore, char ->
                totalScore * 5.toBigInteger() + when (char) {
                    ')' -> 1.toBigInteger()
                    ']' -> 2.toBigInteger()
                    '}' -> 3.toBigInteger()
                    '>' -> 4.toBigInteger()
                    else -> throw IllegalArgumentException("Unexpected char '$char'")
                }
            }
            println("Line autocomplete score $lineAutocompleteScore")
            autocompleteScores.add(lineAutocompleteScore)
        }
    }

    val middleAutocompleteScore = autocompleteScores.sorted()[autocompleteScores.size / 2]

    println()
    println("Part 1, total syntax error score: $totalSyntaxErrorScore")
    println("Part 2, total autocomplete score: $middleAutocompleteScore")
}

val chunkOpenChars = arrayOf('(', '[', '{', '<')
val chunkCloseChars = arrayOf(')', ']', '}', '>')

class InvalidCharException(
    expected: Char,
    val actual: Char
) : Exception("Expected '$expected', but found '$actual' instead")

class IncompleteLineException(
    val autoComplete: String
) : Exception("Line incomplete, missing the following close chars: '$autoComplete'")

fun validateLine(line: CharSequence) {
    val stack = ArrayDeque<Char>()
    for (char in line) {
        if (char in chunkOpenChars) {
            stack.add(char)
        } else if (char in chunkCloseChars) {
            val lastOpenChar = stack.removeLast()
            val expectedCloseChar = chunkCloseChars[chunkOpenChars.indexOf(lastOpenChar)]
            if (char != expectedCloseChar) {
                throw InvalidCharException(expectedCloseChar, char)
            }
        } else {
            throw IllegalArgumentException("Invalid char '$char' found, is not a chunk char")
        }
    }

    if (stack.isNotEmpty()) {
        throw IncompleteLineException(
            stack.map { chunkCloseChars[chunkOpenChars.indexOf(it)] }.reversed().joinToString("")
        )
    }
}
