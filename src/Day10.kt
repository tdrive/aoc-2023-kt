fun main() {

    /*
    Day 10: Pipe Maze
    https://adventofcode.com/2023/day/10
    */

    /*
    | is a vertical pipe connecting north and south.
    - is a horizontal pipe connecting east and west.
    L is a 90-degree bend connecting north and east.
    J is a 90-degree bend connecting north and west.
    7 is a 90-degree bend connecting south and west.
    F is a 90-degree bend connecting south and east.
    . is ground; there is no pipe in this tile.
    S is the starting position of the animal
    */

    /*
    -L|F7
    7S-7|
    L|7||
    -L-J|
    L|-JF
    */

    fun part1(input: List<String>): Int {
        val playingFieldMatrix: List<List<Char>> = createPlayingFieldMatrix(input)
        val startingPointPosition = findStartingPointPosition(playingFieldMatrix)

        val startingPoint = Pipe(
            getShapeFromPosition(
                startingPointPosition,
                playingFieldMatrix
            )!!, startingPointPosition
        )
        val mazePath: List<Pipe> = getMazePath(startingPoint, playingFieldMatrix)

        return getFarthestPoint(mazePath.size)
    }

    //TODO: implement part2 later
    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    part1(testInput).println()
    check(part1(testInput) == 4)

    val testInput2 = readInput("Day10_test2")
    part1(testInput2).println()
    check(part1(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
    //part2(input).println()
}

fun getShapeFromPosition(
    leftPipePosition: Pair<Int, Int>,
    fieldMatrix: List<List<Char>>
) = try {
    Shape.from(fieldMatrix[leftPipePosition.first][leftPipePosition.second])
} catch (e: IndexOutOfBoundsException) {
    null
}

fun getPipesAroundPosition(
    position: Pair<Int, Int>,
    fieldMatrix: List<List<Char>>
): List<Pipe?> = getNeighborsAroundPosition(position).map {
    getShapeFromPosition(it, fieldMatrix).let { shape ->
        if (shape != null) Pipe(
            shape,
            it
        ) else null
    }
}

fun findStartingPointPosition(playingField: List<List<Char>>): Pair<Int, Int> {
    playingField.forEachIndexed { index, chars ->
        if (START_POINT in chars) return index to chars.indexOf(START_POINT)
    }
    throw IllegalArgumentException("Playing field doesn't contain a starting point")
}

fun getMazePath(
    startingPoint: Pipe,
    playingFieldMatrix: List<List<Char>>
): List<Pipe> {

    val resultPath = mutableListOf(startingPoint)
    var nextPipe: Pipe = startingPoint

    while (true) {
        val pipesAround = getPipesAroundPosition(
            nextPipe.position,
            playingFieldMatrix
        )

        val previousPipe = resultPath.last().shape

        val connectedPipes = mutableListOf<Pipe>()
        pipesAround.forEachIndexed { index, pipe ->
            pipe?.let {
                when (index) {
                    INDEX_LEFT -> if (previousPipe.hasContinuationAtLeft(pipe)) connectedPipes.add(pipe)
                    INDEX_TOP -> if (previousPipe.hasContinuationAtTop(pipe)) connectedPipes.add(pipe)
                    INDEX_RIGHT -> if (previousPipe.hasContinuationAtRight(pipe)) connectedPipes.add(pipe)
                    INDEX_BOTTOM -> if (previousPipe.hasContinuationAtBottom(pipe)) connectedPipes.add(pipe)
                }
            }
        }

        connectedPipes.firstOrNull { it !in resultPath }?.let {
            nextPipe = it
            resultPath.add(it)
        } ?: break

    }

    return resultPath
}

const val START_POINT = 'S'
const val INDEX_LEFT = 0
const val INDEX_TOP = 1
const val INDEX_RIGHT = 2
const val INDEX_BOTTOM = 3

data class Pipe(val shape: Shape, val position: Pair<Int, Int>)

sealed interface Shape {
    fun hasContinuationAtLeft(pipe: Pipe): Boolean
    fun hasContinuationAtTop(pipe: Pipe): Boolean
    fun hasContinuationAtRight(pipe: Pipe): Boolean
    fun hasContinuationAtBottom(pipe: Pipe): Boolean

    companion object {
        fun from(ch: Char): Shape? {
            return when (ch) {
                'S' -> StartingPoint
                '-' -> HorizontalLine
                '|' -> VerticalLine
                '7' -> SWBend
                'F' -> SEBend
                'L' -> NEBend
                'J' -> NWBend
                else -> null
            }
        }

        data object StartingPoint : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is SEBend || pipe.shape is SWBend || pipe.shape is StartingPoint //TODO: remove || pipe.shape is StartingPoint

            override fun hasContinuationAtBottom(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is NWBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtLeft(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is SEBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtRight(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is NWBend || pipe.shape is SWBend || pipe.shape is StartingPoint
        }

        data object HorizontalLine : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) = false
            override fun hasContinuationAtBottom(pipe: Pipe) = false
            override fun hasContinuationAtLeft(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is SEBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtRight(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is NWBend || pipe.shape is SWBend || pipe.shape is StartingPoint
        }

        data object VerticalLine : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is SEBend || pipe.shape is SWBend || pipe.shape is StartingPoint

            override fun hasContinuationAtBottom(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is NWBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtLeft(pipe: Pipe) = false
            override fun hasContinuationAtRight(pipe: Pipe) = false
        }

        data object NEBend : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is SEBend || pipe.shape is SWBend || pipe.shape is StartingPoint

            override fun hasContinuationAtBottom(pipe: Pipe) = false
            override fun hasContinuationAtLeft(pipe: Pipe) = false
            override fun hasContinuationAtRight(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is NWBend || pipe.shape is SWBend || pipe.shape is StartingPoint
        }

        data object NWBend : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is SEBend || pipe.shape is SWBend || pipe.shape is StartingPoint

            override fun hasContinuationAtBottom(pipe: Pipe) = false
            override fun hasContinuationAtLeft(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is SEBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtRight(pipe: Pipe) = false
        }

        data object SWBend : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) = false
            override fun hasContinuationAtBottom(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is NWBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtLeft(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is SEBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtRight(pipe: Pipe) = false
        }

        data object SEBend : Shape {
            override fun hasContinuationAtTop(pipe: Pipe) = false
            override fun hasContinuationAtBottom(pipe: Pipe) =
                pipe.shape is VerticalLine || pipe.shape is NWBend || pipe.shape is NEBend || pipe.shape is StartingPoint

            override fun hasContinuationAtLeft(pipe: Pipe) = false
            override fun hasContinuationAtRight(pipe: Pipe) =
                pipe.shape is HorizontalLine || pipe.shape is NWBend || pipe.shape is SWBend || pipe.shape is StartingPoint
        }
    }
}

fun getFarthestPoint(totalDistance: Int) =
    totalDistance / 2