import java.io.File

fun main() {
    val relationShips = File("input/day12.txt").readLines()
        .map {
            val (left, right) = it.split('-')
            left to right
        }

    val graph = Graph()
    relationShips.forEach { graph.addRelation(it) }

    val possiblePaths = graph.distinctPathToEndSequence(false)
        .map { node -> node.joinToString(",") { it.name } }
        .toMutableList()
        .sorted()

    possiblePaths.forEach { println(it) }

    println()
    println("Part 1, found ${possiblePaths.size} paths")
    println()

    val possiblePathsWithSingleDupe = graph.distinctPathToEndSequence(true)
        .map { node -> node.joinToString(",") { it.name } }
        .toMutableList()
        .distinct()
        .sorted()

    possiblePathsWithSingleDupe.forEach { println(it) }

    println()
    println("Part 2, found ${possiblePathsWithSingleDupe.size} paths")
}

private class Graph {
    private val nodeNameMap = mutableMapOf<String, Node>()

    class Node(val name: String) {
        val linkedNodes: MutableSet<Node> = mutableSetOf()
        fun isBigCave() = name[0].isUpperCase()
        fun isStart() = name == "start"
        fun isEnd() = name == "end"
    }

    fun addRelation(relation: Pair<String, String>) {
        val firstNode = nodeNameMap.getOrPut(relation.first) { Node(relation.first) }
        val secondNode = nodeNameMap.getOrPut(relation.second) { Node(relation.second) }

        firstNode.linkedNodes.add(secondNode)
        secondNode.linkedNodes.add(firstNode)
    }

    fun distinctPathToEndSequence(allowSingleDuplicateSmallCaveVisit: Boolean): Sequence<List<Node>> {
        val startNode = nodeNameMap.values.find { it.isStart() } ?: throw Exception("Graph missing start node")
        return distinctPathToEndSequence(listOf(startNode), setOf(startNode), allowSingleDuplicateSmallCaveVisit)
    }

    private fun distinctPathToEndSequence(
        basePath: List<Node>,
        alreadyVisitedNodes: Set<Node>,
        allowSingleDuplicateSmallCaveVisit: Boolean
    ): Sequence<List<Node>> = sequence {
        for (node in basePath.last().linkedNodes) {
            val newBasePath = basePath + node
            if (node.isEnd()) {
                yield(newBasePath)
            } else if (node !in alreadyVisitedNodes) {
                if (node.isBigCave()) {
                    yieldAll(distinctPathToEndSequence(newBasePath, alreadyVisitedNodes, allowSingleDuplicateSmallCaveVisit))
                } else {
                    yieldAll(distinctPathToEndSequence(newBasePath, alreadyVisitedNodes.plus(node), allowSingleDuplicateSmallCaveVisit))
                    if (allowSingleDuplicateSmallCaveVisit) {
                        // Consume the allowed visit
                        yieldAll(distinctPathToEndSequence(newBasePath, alreadyVisitedNodes, false))
                    }

                }
            }
        }
    }
}
