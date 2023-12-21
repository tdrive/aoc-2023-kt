import java.util.PriorityQueue

fun main() {

    /*
    Day 17: Clumsy Crucible
    https://adventofcode.com/2023/day/17
    */

    fun part1(input: List<String>): Int {

        val playingField = createPlayingFieldMatrix(input)

        val start = getStartCell(playingField)
        val finish = getFinishCell(playingField)

        return getCheapestPathToTheEnd(start, finish, playingField) ?: 0
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    part1(testInput).println()

    val testInput2 = readInput("Day17_test2")
    part1(testInput2).println()

    val testInput3 = readInput("Day17_test3")
    part1(testInput3).println()
}
fun getCheapestPathToTheEnd(
    start: Cell,
    finish: Cell,
    playingField: List<List<Char>>
): Int? {
    val cameFrom = mutableMapOf<Cell, Cell?>()
    val costs = mutableMapOf<Cell, Int>()

    val cellQueue = PriorityQueue<Cell>(compareBy { it.cost })
    cellQueue.add(start)
    cameFrom[start] = null
    costs[start] = 0 // you don't incur starting block's heat loss unless you leave that block and then return to it.

    while (cellQueue.isNotEmpty()) {
        val currentCell = cellQueue.poll()

        if (currentCell == finish) {
            break
        }

        val neighbors = getNeighbourCellsAroundPosition(currentCell.position, playingField)
        for (n in neighbors) {
            val newCost = (getCellCost(costs, currentCell)) + n.cost

            if (n !in costs || newCost < getCellCost(costs, n)) {
                costs[n] = newCost
                cellQueue.add(n)
                cameFrom[n] = currentCell
            }
        }
    }

    return costs[finish]
}
data class Cell(val position: Pair<Int, Int>, val cost: Int)

//TODO: take into consideration task limitations as to available cells
/*
    The crucible can move at most three blocks in a single direction before it must turn 90 degrees left or right.
    The crucible also can't reverse direction;
    after entering each city block, it may only turn left, continue straight, or turn right.
*/
fun getNeighbourCellsAroundPosition(
    position: Pair<Int, Int>,
    fieldMatrix: List<List<Char>>
): List<Cell> = getNeighborsAroundPosition(position).mapNotNull { (x, y) ->
    if (x in fieldMatrix.indices && y in fieldMatrix[0].indices)
        Cell(Pair(x, y), fieldMatrix[x][y].digitToInt())
    else null
}

fun getStartCell(playingField: List<List<Char>>): Cell =
    Cell(0 to 0, playingField[0][0].digitToInt())

fun getFinishCell(playingField: List<List<Char>>): Cell =
    Cell(
        playingField.lastIndex to playingField[0].lastIndex,
        playingField[playingField.lastIndex][playingField[0].lastIndex].digitToInt()
    )

fun getCellCost(costs: Map<Cell, Int>, cell: Cell) = costs[cell] ?: 0