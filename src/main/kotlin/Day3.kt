object Day3 : AdventDay() {
  @OptIn(ExperimentalStdlibApi::class)
  override fun solve() {
    val lines = getInputLines() ?: exit()

    lines.map {
      val half = it.length / 2
      Pair(
        it.substring(0, half).chars,
        it.substring(half).chars
      )
    }.sumOf { (fst, snd) ->
      (fst intersect snd).single().rank
    }.printIt()

    lines
      .map { it.toSet() }
      .windowed(3, step = 3)
      .sumOf { (f, s, t) -> (f intersect s intersect t).single().rank }
      .printIt()
  }
}

private val String.chars: Set<Char>
  get() = toCharArray().toSet()

private val Char.rank: Int
  get() = when (this) {
    in 'a'..'z' -> this.code - 'a'.code + 1
    in 'A'..'Z' -> this.code - 'A'.code + 27
    else -> exit()
  }
