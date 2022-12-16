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

private data class HillMapNode(val x: Int, val y: Int)

private data class HillMap(
  val graph: WeightedGraph<HillMapNode>,
  val s: HillMapNode,
  val e: HillMapNode,
  val atA: List<HillMapNode>,
)

private fun List<String>.toHillMap(): HillMap {
  var s: HillMapNode? = null
  var e: HillMapNode? = null
  val atA = mutableListOf<HillMapNode>()

  val graph = flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
      if (c.elevation == 'a') atA += HillMapNode(x, y)
      when (c) {
        'S' -> s = HillMapNode(x, y)
        'E' -> e = HillMapNode(x, y)
        else -> Unit
      }
      HillMapNode(x, y) to listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1).mapNotNull { (cx, cy) ->
        val d = HillMapNode(x + cx, y + cy)
        val cAdj = getOrNull(d.y)?.getOrNull(d.x) ?: return@mapNotNull null
        if (cAdj.elevation.code - c.elevation.code <= 1) E(d, w = 1) else null
      }
    }
  }.toMap().let { adj -> WeightedGraph(adj) }

  return HillMap(graph, s!!, e!!, atA)
}

