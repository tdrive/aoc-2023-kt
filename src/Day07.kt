fun main() {

    // Labels: A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2
    // Hand types: Five of a kind, Four of a kind, Full house, Three of a kind, Two pair, One pair, High card

    fun part1(input: List<String>): Int {

        val sortedHandsMap: Map<Int, List<Pair<String, String>>> =
            input.asSequence()
                .map { str -> str.split(" ") }
                .map { handStrings -> handStrings.first() to handStrings.last() }
                .groupBy { handPair -> handPair.first.toSet().size }

        val handsWithTypes: MutableList<Hand> = mutableListOf()

        sortedHandsMap.forEach {
            parseHands(it, handsWithTypes)
        }

        val sortedHands = handsWithTypes.sortedWith(HandComparator()).reversed()

        return calculateWinnings(sortedHands)
    }

    //TODO: implement part 2
    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val testInput0 = readInput("Day07_test2")
    check(part1(testInput0) == 6592)

    // print first part task result
    val taskInput = readInput("Day07")
    part1(taskInput).println()
}

fun parseHands(
    handWithKey: Map.Entry<Int, List<Pair<String, String>>>,
    handsWithTypes: MutableList<Hand>
) {
    handWithKey.value.forEach { handPair ->
        val handType = when {
            handWithKey.key.isHighCard() -> HighCard
            handWithKey.key.isOnePair() -> OnePair
            handWithKey.key.isFiveOfAKind() -> FiveOfAKind
            else -> {
                val countMap = handPair.first.groupingBy { it }
                    .eachCount()
                    .toList()
                    .sortedByDescending { it.second }

                when (countMap.first().second) {
                    4 -> FourOfAKind
                    3 -> if (countMap.last().second == 2) FullHouse else ThreeOfAKind
                    else -> TwoPairs
                }
            }
        }

        handsWithTypes.add(buildHand(handPair, handType))
    }
}


// Calculate total winnings
fun calculateWinnings(input: List<Hand>): Int {
    var position = 0
    return input.fold(0) { acc, hand ->
        val multiplier = input.size - position
        val winning = hand.bid * multiplier
        position++
        acc + winning
    }
}

private fun buildHand(pair: Pair<String, String>, type: HandValue) =
    Hand(pair.first, pair.second.toInt(), type)

class HandComparator : Comparator<Hand> {
    override fun compare(handOne: Hand?, handTwo: Hand?): Int {
        if (handOne?.type == handTwo?.type) {
            handOne?.hand?.zip(handTwo?.hand!!)?.map {
                if (LABELS.indexOf(it.first) < LABELS.indexOf(it.second)) return 1
                if (LABELS.indexOf(it.first) > LABELS.indexOf(it.second)) return -1
            }
            return 0
        } else {
            return compareHandsType(handOne?.type!!, handTwo?.type!!)
        }
    }

    private fun compareHandsType(handOneType: HandValue, handTwoType: HandValue): Int {
        return when (handOneType) {
            HighCard -> -1
            FiveOfAKind -> 1
            else -> {
                if (handTypeList.indexOf(handOneType) < handTypeList.indexOf(handTwoType)) 1 else -1
            }
        }
    }
}

const val LABELS = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2"
const val LABELS2 = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J"

data class Hand(val hand: String, val bid: Int, val type: HandValue)
sealed interface HandValue
data object FiveOfAKind : HandValue
data object FourOfAKind : HandValue
data object FullHouse : HandValue
data object ThreeOfAKind : HandValue
data object TwoPairs : HandValue
data object OnePair : HandValue
data object HighCard : HandValue

val handTypeList = listOf(
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPairs,
    OnePair,
    HighCard
)

fun Int.isHighCard(): Boolean = this == 5
fun Int.isOnePair(): Boolean = this == 4
fun Int.isFiveOfAKind(): Boolean = this == 1