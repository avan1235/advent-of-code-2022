import kotlin.collections.List

object Day12 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()

    val map = lines.toHillMap()
    val paths = map.graph.reversed().shortestPathsLengths(map.e)

    paths[map.s].printIt()
    map.atA.minOf { paths[it] }.printIt()
  }
}

private val Char.elevation: Char
  get() = when (this) {
    'S' -> 'a'
    'E' -> 'z'
    else -> this
  }

private data class HillMap(val graph: WeightedGraph, val s: N, val e: N, val atA: List<N>)

private fun List<String>.toHillMap(): HillMap {
  var s: N? = null
  var e: N? = null
  val atA = mutableListOf<N>()

  val graph = flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
      if (c.elevation == 'a') atA += N(x, y)
      when (c) {
        'S' -> s = N(x, y)
        'E' -> e = N(x, y)
        else -> Unit
      }
      N(x, y) to listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1).mapNotNull { (cx, cy) ->
        val d = N(x + cx, y + cy)
        val cAdj = getOrNull(d.y)?.getOrNull(d.x) ?: return@mapNotNull null
        if (cAdj.elevation.code - c.elevation.code <= 1) E(d, w = 1) else null
      }
    }
  }.toMap().let { adj -> WeightedGraph(adj) }

  return HillMap(graph, s!!, e!!, atA)
}

