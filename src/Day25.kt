import java.io.File

fun main() {

    /*
    Day 25: Snowverload
    https://adventofcode.com/2023/day/25
    */

    /*
    Test data:
    disconnect the wire between hfx/pzl, the wire between bvb/cmg, and the wire between nvd/jqt
    disconnect three wires to divide the components into two separate, disconnected groups.
        9 components: cmg, frs, lhk, lsr, nvd, pzl, qnr, rsh, and rzs.
        6 components: bvb, hfx, jqt, ntq, rhn, and xhk.
        Multiplying the sizes of these groups together produces 54.
    */

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day25_test")
    buildGraphVizualization("Day25pt1_test", testInput)

    val input = readInput("Day25")
    buildGraphVizualization("Day25pt1", input)
}

// Graphviz should be installed in the system to generate PNG image
fun buildGraphVizualization(graphName: String, input: List<String>) {
    val nodes = getNodes(input)
    val graph = buildGraph(input)

    val nodesNotInGraph = nodes.filterNot { it in graph.keys }

    val fullGraph =
        graph + graph.entries.filter { entry -> entry.value.any { it in nodesNotInGraph } }
            .flatMap { entry ->
                nodesNotInGraph
                    .filter { it in entry.value }
                    .map { it to entry.key }
            }
            .groupBy(
                { it.first },
                { it.second })

    // Build Graphviz DOT graph representation
    val dotGraph = buildDotGraph(fullGraph)

    // Save Graphviz DOT graph to a file
    File("${graphName}.dot").writeText(dotGraph)

    // Visualize the DOT graph using Graphviz
    visualizeGraph(graphName, dotGraph)
}

fun getNodes(input: List<String>): Set<String> =
    input.flatMap {
        it.filterNot { str -> str == ':' }
            .split(' ')
    }.toSet()

fun buildGraph(input: List<String>): Map<String, List<String>> =
    input.map {
        it.filterNot { ch -> ch == ':' }
            .split(' ')
    }.associate {
        it.first() to it.subList(1, it.size)
    }

fun buildDotGraph(dependencyMap: Map<String, List<String>>): String {
    val stringBuilder = StringBuilder()

    stringBuilder.appendLine("digraph G {")

    for ((cluster, dependencies) in dependencyMap) {
        // Add nodes for each cluster
        stringBuilder.appendLine("  subgraph $cluster {")
        stringBuilder.appendLine("    label = \"$cluster\";")
        for (dependency in dependencies) {
            // Add edges between clusters
            stringBuilder.appendLine("    $cluster -> $dependency;")
        }
        stringBuilder.appendLine("  }")
    }

    stringBuilder.appendLine("}")

    return stringBuilder.toString()
}

fun visualizeGraph(graphName: String, dotGraph: String) {
    val processBuilder = ProcessBuilder(
        "dot",
        "-Kneato",
        "-Gdpi=600",
        "-Tpng",
        "-o",
        "${graphName}.png"
    )
    processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE)

    val process = processBuilder.start()
    process.outputStream.bufferedWriter().use { it.write(dotGraph) }

    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("Graph visualization successful. Check ${graphName}.png.")
    } else {
        println("Error in graph visualization.")
    }
}