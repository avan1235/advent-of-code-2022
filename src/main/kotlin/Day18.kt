object Day18 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val coords = lines.mapTo(HashSet()) { it.toV3() }

    DIRECTIONS.sumOf { d -> coords.count { it + d !in coords } }.printIt()

    val air = coords.collectAir()
    DIRECTIONS.sumOf { d -> coords.count { it + d in air } }.printIt()
  }
}

private fun Set<V3>.collectAir(): Set<V3> {
  val bound = Pair(
    V3(minOf { it.x } - 1, minOf { it.y } - 1, minOf { it.z } - 1),
    V3(maxOf { it.x } + 1, maxOf { it.y } + 1, maxOf { it.z } + 1))

  operator fun Pair<V3, V3>.contains(v: V3): Boolean =
    v.x in first.x..second.x && v.y in first.y..second.y && v.z in first.z..second.z

  tailrec fun dfs(p: V3, queue: ArrayDeque<V3> = ArrayDeque(), seen: HashSet<V3> = HashSet()): HashSet<V3> {
    if (p !in seen && p !in this && p in bound) {
      seen += p
      for (d in DIRECTIONS) queue += p + d
    }
    return dfs(queue.removeLastOrNull() ?: return seen, queue, seen)
  }
  return dfs(bound.first)
}

private val DIRECTIONS: List<V3> = listOf(
  V3(x = 0, y = 0, z = 1),
  V3(x = 0, y = 0, z = -1),
  V3(x = 0, y = 1, z = 0),
  V3(x = 0, y = -1, z = 0),
  V3(x = 1, y = 0, z = 0),
  V3(x = -1, y = 0, z = 0),
)

private data class V3(val x: Int, val y: Int, val z: Int)

private operator fun V3.plus(o: V3): V3 =
  V3(x + o.x, y + o.y, z + o.z)

private fun String.toV3(): V3 =
  split(",").map { it.toInt() }.let { (x, y, z) -> V3(x, y, z) }

