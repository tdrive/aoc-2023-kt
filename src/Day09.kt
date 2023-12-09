fun main() {

    /*
    0 3 6 9 12 15
    1 3 6 10 15 21
    10 13 16 21 30 45
    */

    /*
    0   3   6   9  12  15
      3   3   3   3   3
        0   0   0   0
    */

    fun part1(input: List<String>): Int {
        val result = input
            .asSequence()
            .map { str ->
                str.split(" ")
                    .map { numStr -> numStr.toInt() }
            }
            .map {
                calculateExtrapolatedResult(it)
            }
            .sum()

        return result
    }

    //TODO: implement part2
    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()

    part2(input).println()
}

fun calculateExtrapolatedResult(sequence: List<Int>): Int {
    val sequencesMatrix: MutableList<MutableList<Int>> =
        mutableListOf(sequence.toMutableList())

    extrapolateRecursively(sequencesMatrix)

    return if (sequencesMatrix.size == 1) sequencesMatrix.first().last()
    else sequencesMatrix.reversed().sumOf { it.last() }
}
tailrec fun extrapolateRecursively(extrapolationMatrix: MutableList<MutableList<Int>>) {
    val listOfDifferences = getSequenceOfDifferences(extrapolationMatrix.last())

    if (listOfDifferences.isFinalSequence()) {
        val sequenceToExtrapolate = extrapolationMatrix.last()
        sequenceToExtrapolate.add(listOfDifferences.first() + sequenceToExtrapolate.last())
    } else {
        extrapolationMatrix.add(listOfDifferences.toMutableList())
        extrapolateRecursively(extrapolationMatrix)
    }
}
fun getSequenceOfDifferences(sequence: List<Int>): List<Int> {
    return sequence.mapIndexed { index, num ->
        if (index > 0) {
            num - sequence[index - 1]
        } else 0
    }.drop(1)
}
fun List<Int>.isFinalSequence() = this.toSet().size == 1