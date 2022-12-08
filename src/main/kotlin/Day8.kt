import kotlin.math.max

object Day8 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val map = lines.buildMap()
    val m = lines.first().length
    val n = lines.size

    map.countVisible(m, n).printIt()
    map.calcScenicScore(m, n).printIt()
  }
}

private typealias CoordinateMap<T> = LazyDefaultMap<Int, DefaultMap<Int, T>>

private fun List<String>.buildMap() = coordinateMap(-1).also { map ->
  forEachIndexed { y, line ->
    line.forEachIndexed { x, c ->
      map[y][x] = c.digitToIntOrNull()!!
    }
  }
}

private fun <T> coordinateMap(default: T): CoordinateMap<T> =
  LazyDefaultMap(default = { DefaultMap(default) })

private fun CoordinateMap<Int>.countVisible(m: Int, n: Int): Int = coordinateMap(false).also { visible ->
  var currHighest = -1
  fun update(x: Int, y: Int) {
    visible[y][x] = visible[y][x] || this[y][x] > currHighest
    currHighest = max(this[y][x], currHighest)
  }

  for (x in 0 until m) {
    currHighest = -1
    for (y in 0 until n) update(x, y)
  }
  for (x in 0 until m) {
    currHighest = -1
    for (y in n - 1 downTo 0) update(x, y)
  }
  for (y in 0 until n) {
    currHighest = -1
    for (x in 0 until m) update(x, y)
  }
  for (y in 0 until n) {
    currHighest = -1
    for (x in m - 1 downTo 0) update(x, y)
  }
}.values.sumOf { row -> row.values.count { it } }

private fun CoordinateMap<Int>.calcScenicScore(m: Int, n: Int): Int {
  var maxScore = -1

  for (startX in 0 until m) {
    for (startY in 0 until n) {
      val l = (startX - 1 downTo 0).countTakingUnless { x -> this[startY][x] >= this[startY][startX] }
      val r = (startX + 1 until m).countTakingUnless { x -> this[startY][x] >= this[startY][startX] }
      val u = (startY - 1 downTo 0).countTakingUnless { y -> this[y][startX] >= this[startY][startX] }
      val d = (startY + 1 until n).countTakingUnless { y -> this[y][startX] >= this[startY][startX] }
      maxScore = max(maxScore, l * r * u * d)
    }
  }

  return maxScore
}

private fun IntProgression.countTakingUnless(predicate: (Int) -> Boolean): Int {
  var result = 0
  for (v in this) {
    result += 1
    if (predicate(v)) break
  }
  return result
}
