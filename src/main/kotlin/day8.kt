import java.io.File

fun main() {
    val lines = File("input/day8.txt").readLines()

    val state = lines.map { line ->
        val (examples, data) = line.split(" | ")
        Pair(
            examples.split(' ').map { it.toSortedSet().joinToString("") },
            data.split(' ').map { it.toSortedSet().joinToString("") }
        )
    }

    val part1Answer = state.sumOf { (_, line) -> line.count { detectNum(it) != null } }
    println("Part 1, $part1Answer occurrences of 1,4,7,8")

    val totalSum = state.sumOf { (examples, data) ->
        val displayMap = mutableMapOf<Int, String>()

        // Map known (by length) segment combinations (1, 4, 7, 8)
        examples.forEach {
            when (it.length) {
                2 -> displayMap[1] = it
                3 -> displayMap[7] = it
                4 -> displayMap[4] = it
                7 -> displayMap[8] = it
            }
        }

        val fiveSegmentExamples = examples.filter { it.length == 5 }.toMutableList()
        val sixSegmentExamples = examples.filter { it.length == 6 }.toMutableList()

        // 9 is the only six-segment number that has the segments of both 4 and 7
        displayMap[9] = sixSegmentExamples.first {
            it.containsAllCharsIn(displayMap[4]!!) && it.containsAllCharsIn(displayMap[7]!!)
        }
        sixSegmentExamples.remove(displayMap[9]!!)

        // 0 is the only remaining six-segment number with segments of 7
        displayMap[0] = sixSegmentExamples.first { it.containsAllCharsIn(displayMap[7]!!) }
        sixSegmentExamples.remove(displayMap[0]!!)

        // 6 should be the only six-segment number remaining
        displayMap[6] = sixSegmentExamples.first()


        // 3 is the only five-segment number with segments of 7
        displayMap[3] = fiveSegmentExamples.first { it.containsAllCharsIn(displayMap[7]!!) }
        fiveSegmentExamples.remove(displayMap[3]!!)

        // segments are as follows:
        //  aaaa
        // b    c
        // b    c
        //  dddd
        // e    f
        // e    f
        //  gggg
        // 5 is found by finding the diff between 3 and 0 (b,c,e) and diffing that with 6 (a,f,g)
        val fiveSearchSegments = displayMap[3]!!.diff(displayMap[0]!!).diff(displayMap[6]!!)
        displayMap[5] = fiveSegmentExamples.first { it.containsAllCharsIn(fiveSearchSegments) }
        fiveSegmentExamples.remove(displayMap[5]!!)

        // 2 is the only remaining five-segment number
        displayMap[2] = fiveSegmentExamples.first()

        val wireToNumberMap = displayMap.entries.associateBy({ it.value }, { it.key })

        data.map { wireToNumberMap[it]!! }.joinToString("").toInt()
    }

    println("Part 2, sum of output values: $totalSum")
}

fun String.containsAllCharsIn(string: String): Boolean = string.all { this.contains(it) }

/**
 * Calculate symmetric difference between strings (only keep chars that occur in 1 of the two strings)
 */
fun String.diff(other: String): String {
    val leftUniq = this.filter { it !in other }
    val rightUniq = other.filter { it !in this }
    return (leftUniq + rightUniq).toSortedSet().joinToString("")
}


fun detectNum(data: String): Int? = when (data.length) {
    2 -> 1
    4 -> 4
    3 -> 7
    7 -> 8
    else -> null
}
