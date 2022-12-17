import java.util.Arrays
import java.util.TreeSet

object Day17 : AdventDay() {
  override fun solve() {
    val line = getInputLines()?.single() ?: exit()
    val moves = line.toRockMoves()

    val simulation = RockSimulation(moves)
    simulation.calculateHeight(count = 2022).printIt()
    simulation.calculateHeight(count = 1_000_000_000_000).printIt()
  }
}

private fun String.toRockMoves(): List<RockMove> = toCharArray().map {
  when (it) {
    '>' -> RockMove.RIGHT
    '<' -> RockMove.LEFT
    else -> exit()
  }
}

private enum class RockMove(val v: V2) {
  DOWN(v = V2(0, -1)),
  LEFT(v = V2(-1, 0)),
  RIGHT(v = V2(1, 0)),
}

private class RockSimulation(private val moves: List<RockMove>) {

  private val stoppedRocks: TreeSet<RockPart> = TreeSet(compareBy<RockPart>({ it.x }, { it.y }).reversed())

  private var moveIdx: Int = 0
    get() = field.also { field = (field + 1) % moves.size }

  private var rockIdx: Int = 0
    get() = field.also { field = (field + 1) % ROCKS.size }

  private val startPosition: V2 get() = V2(3, heights.last() + 4)

  private val heights: MutableList<Int> = mutableListOf(0)

  private val cycle: Int = ROCKS.size * 2

  init {
    simulateFallingRocks()
  }

  fun calculateHeight(count: Long): Long {
    val period = heights.size / cycle + 1
    val eachPeriodHeight = heights[2 * period] - heights[period]
    val restHeight = heights[(count % period).toInt()]
    return count / period * eachPeriodHeight + restHeight
  }

  override fun toString(): String = buildString {
    appendLine("+-------+")
    val maxY = stoppedRocks.maxOfOrNull { it.y } ?: -1
    val partsByYX = stoppedRocks
      .groupBy { it.y }
      .mapValues { it.value.groupBy { part -> part.x }.mapValues { e -> e.value.single() } }
      .toDefaultMap(emptyMap())
    for (y in 1..maxY) {
      append('|')
      for (x in 1..7) append(if (partsByYX[y][x] == null) '.' else '#')
      appendLine('|')
    }
    appendLine("|.......|")
  }.lines().asReversed().joinToString(separator = "\n")

  private fun simulateFallingRocks() {
    while (true) {
      val rock = simulateFallingRock()
      heights += maxOf(heights.last(), rock.parts.maxOf { it.y })

      if (heights.size > cycle * (cycle - 1) && heights.size % cycle == 0) {
        val foundCycle = (1 until cycle)
          .map { heights[(heights.size / cycle + 1) * it - 1] }
          .zipWithNext { fst, snd -> snd - fst }
          .toHashSet()
          .size == 1
        if (foundCycle) break
      }
    }
  }

  private fun simulateFallingRock(): Rock {
    var rock = ROCKS[rockIdx].move(startPosition)

    while (true) {
      val move = moves[moveIdx]

      val movedByJet = rock.move(move)
      if (!movedByJet.isStopped) rock = movedByJet

      val movedDown = rock.move(RockMove.DOWN)
      if (movedDown.isStopped) break

      rock = movedDown
    }
    stoppedRocks += rock.parts
    return rock
  }

  private val RockPart.isOnBorder: Boolean
    get() = x == 0 || y == 0 || x == 8

  private val Rock.isOnBorder: Boolean
    get() = parts.any { it.isOnBorder }

  private val RockPart.isOnStoppedRock: Boolean
    get() = stoppedRocks.any { it.x == x && it.y == y }

  private val Rock.isOnStoppedRock: Boolean
    get() = parts.any { it.isOnStoppedRock }

  private val Rock.isStopped: Boolean
    get() = isOnBorder || isOnStoppedRock
}

private data class RockPart(val x: Int, val y: Int) {
  fun move(v: V2): RockPart = RockPart(x + v.first, y + v.second)

  override fun hashCode(): Int = y * 9 + (x - 1)

  override fun equals(other: Any?): Boolean =
    (other as? RockPart)?.let { it.x == x && it.y == y } == true
}

private data class Rock(val parts: List<RockPart>, val rests: Boolean = false) {
  fun move(v: V2): Rock = copy(parts = parts.map { it.move(v) })
  fun move(m: RockMove): Rock = move(m.v)
}

private val ROCKS = listOf(
  Rock(
    parts = listOf(
      RockPart(x = 0, y = 0),
      RockPart(x = 1, y = 0),
      RockPart(x = 2, y = 0),
      RockPart(x = 3, y = 0),
    )
  ), Rock(
    parts = listOf(
      RockPart(x = 1, y = 0),
      RockPart(x = 0, y = 1),
      RockPart(x = 1, y = 1),
      RockPart(x = 2, y = 1),
      RockPart(x = 1, y = 2),
    )
  ), Rock(
    parts = listOf(
      RockPart(x = 0, y = 0),
      RockPart(x = 1, y = 0),
      RockPart(x = 2, y = 0),
      RockPart(x = 2, y = 1),
      RockPart(x = 2, y = 2),
    )
  ), Rock(
    parts = listOf(
      RockPart(x = 0, y = 0),
      RockPart(x = 0, y = 1),
      RockPart(x = 0, y = 2),
      RockPart(x = 0, y = 3),
    )
  ), Rock(
    parts = listOf(
      RockPart(x = 0, y = 0),
      RockPart(x = 0, y = 1),
      RockPart(x = 1, y = 0),
      RockPart(x = 1, y = 1),
    )
  )
)
