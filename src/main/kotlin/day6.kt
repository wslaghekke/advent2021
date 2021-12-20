import java.io.File

fun main() {
    val state = File("input/day6.txt")
        .readLines()
        .flatMap { line -> line.split(',').map { it.toInt() } }
        .toMutableList()


    println("Initial state: ${state.joinToString(",")}")

    val fishState = LanternFishState(state)
    repeat(256) { day ->
        fishState.simulateDay()

        println("After ${(day+1).toString().padStart(2)} days: ${fishState.totalFishCount()} fish")
    }
}

class LanternFishState(
    initialFishes: List<Int>
) {
    private var fish0 = initialFishes.count { it == 0 }.toBigInteger()
    private var fish1 = initialFishes.count { it == 1 }.toBigInteger()
    private var fish2 = initialFishes.count { it == 2 }.toBigInteger()
    private var fish3 = initialFishes.count { it == 3 }.toBigInteger()
    private var fish4 = initialFishes.count { it == 4 }.toBigInteger()
    private var fish5 = initialFishes.count { it == 5 }.toBigInteger()
    private var fish6 = initialFishes.count { it == 6 }.toBigInteger()
    private var fish7 = initialFishes.count { it == 7 }.toBigInteger()
    private var fish8 = initialFishes.count { it == 8 }.toBigInteger()

    fun simulateDay() {
        val prevFish0 = fish0
        val prevFish1 = fish1
        val prevFish2 = fish2
        val prevFish3 = fish3
        val prevFish4 = fish4
        val prevFish5 = fish5
        val prevFish6 = fish6
        val prevFish7 = fish7
        val prevFish8 = fish8

        fish0 = prevFish1
        fish1 = prevFish2
        fish2 = prevFish3
        fish3 = prevFish4
        fish4 = prevFish5
        fish5 = prevFish6
        fish6 = prevFish7 + prevFish0
        fish7 = prevFish8
        fish8 = prevFish0
    }

    fun totalFishCount() = fish0 + fish1 + fish2 + fish3 + fish4 + fish5 + fish6 + fish7 + fish8
}
