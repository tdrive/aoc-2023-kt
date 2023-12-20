import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun createPlayingFieldMatrix(input: List<String>): List<List<Char>> = input.map { it.toList() }

fun getNeighborsAroundPosition(
    position: Pair<Int, Int>
): List<Pair<Int, Int>> {

    val neighbors = mutableListOf<Pair<Int, Int>>()

    val directions = listOf(
        Pair(0, -1), // left
        Pair(-1, 0), // top
        Pair(0, 1), // right
        Pair(1, 0) // bottom
    )

    for ((x, y) in directions) {
        val newRow = position.first + x
        val newColumn = position.second + y

        neighbors.add(Pair(newRow, newColumn))
    }
    return neighbors
}