import java.util.*

object Day5 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val first = lines.takeWhile { it.isNotEmpty() }.map { it.toCharArray() }
    val last = lines.takeLastWhile { it.isNotEmpty() }

    val moves = last.map { it.toCraneMove() }

    val positions = first.toPositions()
    val cranes1 = first.toCranes(positions)
    val cranes2 = cranes1.copy(copyValue = { Stack<String>().apply { addAll(it) } })

    moves.forEach { it.execute(cranes1) }
    cranes1.description.printIt()

    moves.forEach { it.batchExecute(cranes2) }
    cranes2.description.printIt()
  }
}

private typealias Cranes = LazyDefaultMap<Int, Stack<String>>

private typealias Positions = Map<Int, Int>

private typealias Data = List<CharArray>

private fun Data.toPositions(): Positions = last()
  .mapIndexed { idx, c -> if (c.isDigit()) Pair(idx, "$c".toInt()) else null }
  .filterNotNull()
  .toMap()

private val Cranes.description: String
  get() = toList().sortedBy { it.first }.joinToString(separator = "") { it.second.peek() }

private fun Data.toCranes(positions: Positions): Cranes =
  Cranes(::Stack).also { data ->
    reversed().drop(1).forEach {
      for ((idx, p) in positions) {
        val c = it(idx) ?: continue
        data[p].push(c)
      }
    }
  }

private data class CraneMove(val from: Int, val to: Int, val count: Int) {
  fun execute(map: Cranes) {
    repeat(count) {
      val moved = map[from].pop()
      map[to].push(moved)
    }
  }

  fun batchExecute(map: Cranes) {
    val temp = Stack<String>()
    repeat(count) {
      val moved = map[from].pop()
      temp.push(moved)
    }
    repeat(count) {
      val moved = temp.pop()
      map[to].push(moved)
    }
  }
}

private operator fun <T> List<T>.component6(): T = this[5]

private fun String.toCraneMove(): CraneMove =
  split(" ").let { (_, count, _, from, _, to) -> CraneMove(from.toInt(), to.toInt(), count.toInt()) }

private operator fun CharArray.invoke(idx: Int): String? =
  if (idx >= this.size || !this[idx].isLetter()) null else "${this[idx]}"
