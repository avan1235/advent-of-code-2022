object Day6 : AdventDay() {
  override fun solve() {
    val line = getInputLines()?.single() ?: exit()
    val chars = line.toCharArray().toList()
    chars.firstIdxOfDistinct(count = 4).printIt()
    chars.firstIdxOfDistinct(count = 14).printIt()
  }
}

private fun List<Char>.firstIdxOfDistinct(count: Int): Int? = asSequence()
  .windowed(count, step = 1)
  .mapIndexedNotNull { idx, chars ->
    if (chars.toSet().size == count) idx + count
    else null
  }
  .firstOrNull()
