object Day1 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val carry = lines.groupSeparatedBy(
      separator = { line -> line == "" },
      transform = { group -> group.sumOf { it.toInt() } }
    )
    carry.max().printIt()
    carry.sortedDescending().take(3).sum().printIt()
  }
}
