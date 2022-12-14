import kotlin.math.max

object Day14 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val map = lines.buildSandMap()

    map.copy().apply {
      simulateSand { y, _ -> y > map.maxRockY }
      sandUnits.printIt()
    }
    map.copy().apply {
      simulateSand()
      sandUnits.printIt()
    }
  }
}

private fun List<String>.buildSandMap(): SandMap = SandMap().also { map ->
  forEach { line -> line.split(" -> ").map { it.toSandCord() }.fillMap(map) }
}

private fun List<SandCord>.fillMap(map: SandMap) {
  windowed(2, step = 1).map { (from, to) ->
    when {
      from.x == to.x -> (from.y directedTo to.y).forEach { y -> map[y, to.x] = SandMapElement.ROCK }
      from.y == to.y -> (from.x directedTo to.x).forEach { x -> map[to.y, x] = SandMapElement.ROCK }
      else -> exit()
    }
  }
}

private fun String.toSandCord(): SandCord =
  split(",").let { (x, y) -> SandCord(x.toInt(), y.toInt()) }

private data class SandCord(val x: Int, val y: Int)

private class SandMap(
  private val sandSourceX: Int = 500,
  private val sandSourceY: Int = 0,
  private val map: LazyDefaultMap<Int, DefaultMap<Int, SandMapElement>> =
    LazyDefaultMap(default = { DefaultMap(SandMapElement.AIR) }),
  maxRockY: Int = -1,
) {
  var maxRockY: Int = maxRockY
    private set


  fun copy(): SandMap = SandMap(sandSourceX, sandSourceY, map.copy(copyValue = { it.copy() }), maxRockY)

  operator fun get(y: Int, x: Int): SandMapElement =
    if (y == maxRockY + 2) SandMapElement.ROCK else map[y][x]

  operator fun set(y: Int, x: Int, element: SandMapElement) {
    map[y][x] = element
    if (element == SandMapElement.ROCK) maxRockY = max(maxRockY, y)
  }

  fun simulateSand(extraBreakCondition: (Int, Int) -> Boolean = { y, x -> false }) {
    while (simulateSandRound(extraBreakCondition)) Unit
  }

  private fun simulateSandRound(extraBreakCondition: (Int, Int) -> Boolean): Boolean {
    var x = sandSourceX
    var y = sandSourceY
    while (true) {
      if (extraBreakCondition(y, x)) {
        return false
      }
      if (this[y, x].isOccupied()) {
        return false
      }
      if (this[y + 1, x].isOccupied() &&
        this[y + 1, x - 1].isOccupied() &&
        this[y + 1, x + 1].isOccupied()
      ) {
        break
      }
      if (this[y + 1, x].isFree()) {
        y += 1
        continue
      }
      if (this[y + 1, x - 1].isFree()) {
        y += 1
        x -= 1
        continue
      }
      if (this[y + 1, x + 1].isFree()) {
        y += 1
        x += 1
        continue
      }
    }
    this[y, x] = SandMapElement.SAND
    return true
  }

  val sandUnits: Int
    get() = map.values.flatMap { it.values }.count { it == SandMapElement.SAND }

  override fun toString(): String = buildString {
    val xs = map.values.flatMap { it.keys }
    val ys = map.keys
    for (y in ys.min()..ys.max()) {
      for (x in xs.min()..xs.max()) append(this@SandMap[y, x])
      appendLine()
    }
  }
}

private enum class SandMapElement {
  ROCK, AIR, SAND;

  override fun toString(): String = when (this) {
    ROCK -> "#"
    AIR -> "."
    SAND -> "o"
  }

  fun isFree(): Boolean = this == AIR
  fun isOccupied(): Boolean = this != AIR
}
