import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

object Day9 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val moves = lines.map { it.toRopeMove() }
    Simulation(tailLength = 1).apply {
      moves.forEach { stepSimulation(it) }
    }.tailPositionsCount.printIt()
    Simulation(tailLength = 9).apply {
      moves.forEach { stepSimulation(it) }
    }.tailPositionsCount.printIt()
  }
}


private class Simulation(private val tailLength: Int) {

  private var headPosition: V2 = V2(0, 0)
  private var tailPosition: MutableList<V2> = generateSequence { V2(0, 0) }.take(tailLength).toMutableList()
  private val visitedTailPositions: MutableSet<V2> = mutableSetOf(V2(0, 0))
  val tailPositionsCount: Int get() = visitedTailPositions.size

  fun stepSimulation(ropeMove: RopeMove) {
    move@ for (count in 1..ropeMove.count) {
      var currMove = ropeMove.toDirection()
      var lastHead = headPosition
      var currHead = lastHead + currMove
      headPosition = currHead

      for (i in tailPosition.indices) {
        if (currMove == V2(0, 0)) continue@move
        val lastTail = tailPosition[i]
        if (lastHead == lastTail) continue@move
        val (movedHead, movedHeadMove) = getNewTailPositionDirection(currHead, lastHead, lastTail, currMove)
        tailPosition[i] = movedHead
        currHead = movedHead
        currMove = movedHeadMove
        lastHead = lastTail
      }
      visitedTailPositions += tailPosition[tailPosition.size - 1]
    }
  }

  override fun toString(): String = buildString {
    for (y in 15 downTo -5) {
      for (x in -11 until 15) when (val p = V2(x, y)) {
        headPosition -> append('H')
        in tailPosition -> append(tailPosition.indexOf(p) + 1)
        V2(0, 0) -> append('s')
        else -> append('.')
      }
      appendLine()
    }
  }
}

private fun getNewTailPositionDirection(newHead: V2, oldHead: V2, oldTail: V2, headMoved: V2): Pair<V2, V2> {
  val headTail = newHead - oldTail
  return when (headTail.abs) {
    V2(0, 0), V2(1, 0), V2(0, 1) -> Pair(oldTail, V2(0, 0))
    V2(2, 0), V2(0, 2) -> headTail.normalized.let { Pair(oldTail + it, it) }
    V2(1, 1) -> Pair(oldTail, V2(0, 0))
    else -> if (headMoved.length != 1) Pair(oldTail + headMoved, headMoved) else Pair(oldHead, headTail - headMoved)
  }
}

private typealias V2 = Pair<Int, Int>

private operator fun V2.plus(v: V2): V2 = Pair(first + v.first, second + v.second)
private operator fun V2.minus(v: V2): V2 = Pair(first - v.first, second - v.second)
private val V2.length: Int get() = first * first + second * second
private val V2.abs: V2 get() = V2(abs(first), abs(second))
private val V2.normalized: V2 get() = V2(first.sign * min(1, abs(first)), second.sign * min(1, abs(second)))

private sealed interface RopeMove {
  val count: Int

  data class U(override val count: Int) : RopeMove
  data class D(override val count: Int) : RopeMove
  data class L(override val count: Int) : RopeMove
  data class R(override val count: Int) : RopeMove
}

private fun String.toRopeMove(): RopeMove = split(" ").let { (type, count) ->
  val moveCount = count.toInt()
  when (type) {
    "U" -> RopeMove.U(moveCount)
    "D" -> RopeMove.D(moveCount)
    "L" -> RopeMove.L(moveCount)
    "R" -> RopeMove.R(moveCount)
    else -> exit()
  }
}

private fun RopeMove.toDirection(): V2 = when (this) {
  is RopeMove.D -> V2(0, -1)
  is RopeMove.L -> V2(-1, 0)
  is RopeMove.R -> V2(1, 0)
  is RopeMove.U -> V2(0, 1)
}
