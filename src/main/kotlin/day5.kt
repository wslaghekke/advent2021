import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    getPartOne()
    getPartTwo()
}

fun getPartOne() {
    val points = File("input/day5.txt").readLines()
        .asSequence()
        .flatMap { line -> getPointsInline(line, false) }

    val pointMap = mutableMapOf<Pair<Int, Int>, Int>()

    for (point in points) {
        if (pointMap.containsKey(point)) {
            pointMap[point] = pointMap[point]!! + 1
        } else {
            pointMap[point] = 1
        }
    }

    println("Part2: ${pointMap.values.count { it >= 2 }} points with 2 or more points")
}

fun getPartTwo() {
    val points = File("input/day5.txt").readLines()
        .asSequence()
        .flatMap { line -> getPointsInline(line, true) }

    val pointMap = mutableMapOf<Pair<Int, Int>, Int>()

    for (point in points) {
        if (pointMap.containsKey(point)) {
            pointMap[point] = pointMap[point]!! + 1
        } else {
            pointMap[point] = 1
        }
    }

    println("Part1: ${pointMap.values.count { it >= 2 }} points with 2 or more points")
}

fun getPointsInline(line: String, allowDiagonal: Boolean) = sequence {
    val matchResult = "(\\d+),(\\d+) -> (\\d+),(\\d+)-*".toRegex().matchEntire(line)!!.groupValues
    val x1 = matchResult[1].toInt()
    val y1 = matchResult[2].toInt()
    val x2 = matchResult[3].toInt()
    val y2 = matchResult[4].toInt()

    if (x1 == x2) {
        for (y in smartIntRange(y1, y2)) {
            yield(x1 to y)
        }
    } else if (y1 == y2) {
        for(x in smartIntRange(x1, x2)) {
            yield(x to y1)
        }
    } else if (allowDiagonal) {
        // We are assuming only 45 degree angles occur and that the x1-x2 and y1-y2 always have the same difference
        val stepCount = abs(x1 - x2)
        for (offset in 0..stepCount) {
            yield(
                Pair(
                    if(x1 < x2) x1 + offset else x1 - offset,
                    if(y1 < y2) y1 + offset else y1 - offset
                )
            )
        }


    }
}

fun smartIntRange(a: Int,b: Int) = IntRange(min(a,b), max(a,b))