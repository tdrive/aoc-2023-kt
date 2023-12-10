import java.lang.IllegalArgumentException

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

    fun createPlayingFieldMatrix(input: List<String>): List<List<Char>> = input.map { it.toList() }

    fun findStartingPointPosition(playingField: List<List<Char>>): Pair<Int, Int> {
        playingField.forEachIndexed { index, chars ->
            if ('S' in chars) return index to chars.indexOf('S')
        }
        throw IllegalArgumentException("Playing field doesn't contain a starting point")
    }

    fun part1(input: List<String>): Int {
        val playingFieldMatrix: List<List<Char>> = createPlayingFieldMatrix(input)
        playingFieldMatrix.println()
        findStartingPointPosition(playingFieldMatrix).println()
        return input.size
    }

    //TODO: implement part2 later
    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    testInput.println()
    part1(testInput)
    //check(part1(testInput) == 4)

    val testInput2 = readInput("Day10_test2")
    testInput2.println()
    part1(testInput2)
    //check(part1(testInput2) == 8)

    /*    val input = readInput("Day10")
        part1(input).println()
        part2(input).println()*/
}