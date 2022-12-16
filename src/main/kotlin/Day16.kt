import kotlin.math.min

object Day16 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val (aa, nodes) = lines.toScanNodes()
    val dist = nodes.calcDistances()
    val notEmptyFlows = nodes.filter { it.rate > 0 }.map { it.index }
    val dp = NodeRestTimeDp(default = { LazyDefaultMap(default = { DefaultMap(-1) }) })

    dp.dfs1(aa, notEmptyFlows, 30, nodes, dist).printIt()
    dp.dfs2(aa, notEmptyFlows, 26, nodes, dist, aa).printIt()
  }
}

private fun List<ScanNode>.calcDistances(): Array<IntArray> = Array(size) { IntArray(size) { size + 1 } }.also { dist ->
  for (src in this) for (dest in src.leadsTo) dist[src.index][dest] = 1
  for (k in indices) for (i in indices) for (j in indices)
    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
}

private fun NodeRestTimeDp.dfs1(
  curr: Int,
  rest: List<Int>,
  time: Int,
  nodes: List<ScanNode>,
  dist: Array<IntArray>,
): Int =
  if (this[curr][rest][time] >= 0) this[curr][rest][time]
  else sequence {
    yield(0)
    for ((r, rr) in rest.selectEach()) if (dist[curr][r] < time)
      yield(nodes[r].rate * (time - dist[curr][r] - 1) + dfs1(r, rr, time - dist[curr][r] - 1, nodes, dist))
  }.max().also { this[curr][rest][time] = it }

private fun NodeRestTimeDp.dfs2(
  curr: Int,
  rest: List<Int>,
  time: Int,
  nodes: List<ScanNode>,
  dist: Array<IntArray>,
  aa: Int,
): Int = sequence {
  for ((r, rr) in rest.selectEach()) if (dist[curr][r] < time)
    yield(nodes[r].rate * (time - dist[curr][r] - 1) + dfs2(r, rr, time - dist[curr][r] - 1, nodes, dist, aa))
}.maxOrNull() ?: dfs1(aa, rest, 26, nodes, dist)

private typealias NodeRestTimeDp = LazyDefaultMap<Int, LazyDefaultMap<List<Int>, DefaultMap<Int, Int>>>

private fun <T> List<T>.selectEach(): Sequence<Pair<T, List<T>>> = sequence {
  val elems = this@selectEach
  for (i in elems.indices) yield(Pair(elems[i], elems.subList(0, i) + elems.subList(i + 1, elems.size)))
}

private data class ScanNode(val index: Int, val rate: Int, val leadsTo: List<Int>)

private fun List<String>.toScanNodes(): Pair<Int, List<ScanNode>> {
  var currIdx = 0
  val indices = HashMap<String, Int>()
  val nodes = map { line ->
    line.toScanNode {
      if (it in indices) indices[it]!!
      else currIdx.apply { indices[it] = currIdx }.also { currIdx += 1 }
    }
  }.sortedBy { it.index }
  return Pair(indices["AA"]!!, nodes)
}

private fun String.toScanNode(index: (String) -> Int): ScanNode = split(" ").let { parts ->
  ScanNode(
    index = index(parts[1]),
    rate = parts[4].removePrefix("rate=").removeSuffix(";").toInt(),
    leadsTo = parts.drop(9).map { index(it.take(2)) })
}
