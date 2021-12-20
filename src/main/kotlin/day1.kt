import java.io.File

fun main() {
    val lines = File("input/day1.txt").readLines().map { it.toInt() }

    val increaseCount = lines
        .windowed(2)
        .count { (prev, curr) -> curr > prev }

    println("Part1: $increaseCount")

    val part2CountOptimized = lines
        .windowed(4)
        .count { (first, _, _, last) -> last > first }

    println("Part2: $part2CountOptimized")
}