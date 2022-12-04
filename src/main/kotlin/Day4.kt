object Day4 : AdventDay() {
  @OptIn(ExperimentalStdlibApi::class)
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val ranges = lines.map { line ->
      val (l, r) = line.split(",").map { it.toRange() }
      Pair(l, r)
    }

    ranges.count { (l, r) ->
      (l contains r) || (r contains l)
    }.printIt()

    ranges.count { (l, r) ->
      l overlaps r
    }.printIt()
  }
}

private fun String.toRange(): IntRange =
  split("-").map { it.toInt() }.let { (f, s) -> f..s }

private infix fun IntRange.contains(o: IntRange): Boolean =
  this.first <= o.first && this.last >= o.last

private infix fun IntRange.overlaps(o: IntRange): Boolean =
  this contains o || o contains this ||
    (this.first <= o.first && this.last in o) ||
    (this.last >= o.last && this.first in o)
