object Day11 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val monkeys = lines.groupSeparatedBy(
      separator = { it == "" },
      transform = { it.toMonkey() }
    )
    val ids = monkeys.map { it.id }.toList()
    val mod = monkeys.productOf { it.testDivBy }
    val groupedMonkeys = monkeys.groupBy { it.id }.mapValues { it.value.single() }

    groupedMonkeys.simulate(ids, rounds = 20) { it / 3 }.printIt()
    groupedMonkeys.simulate(ids, rounds = 10_000) { it % mod }.printIt()
  }
}

private fun Map<Int, Monkey>.simulate(ids: List<Int>, rounds: Int, extraOp: (Long) -> Long): Long =
  copied(copyValue = { it.copy() }).let { monkeys ->
    repeat(rounds) {
      ids.forEach { id ->
        monkeys[id]!!.processItems(monkeys, extraOp)
      }
    }
    monkeys.values
      .map { it.inspectedItemsCount }
      .sortedDescending()
      .let { (fst, snd) -> fst * snd }
  }


private sealed interface OpItem {
  operator fun invoke(value: Long): Long

  object Old : OpItem {
    override fun invoke(value: Long): Long = value
  }

  data class Num(val value: Long) : OpItem {
    override fun invoke(value: Long): Long = this.value
  }
}

private sealed interface Op {
  operator fun invoke(value: Long): Long
  data class Times(val left: OpItem, val right: OpItem) : Op {
    override fun invoke(value: Long): Long = left(value) * right(value)
  }

  data class Plus(val left: OpItem, val right: OpItem) : Op {
    override fun invoke(value: Long): Long = left(value) + right(value)
  }
}

private fun String.toOpItem(): OpItem = if (this == "old") OpItem.Old else OpItem.Num(toLong())

private fun String.toOp(): Op = removePrefix("  Operation: new = ").split(" ").let { (l, op, r) ->
  when (op) {
    "*" -> Op.Times(l.toOpItem(), r.toOpItem())
    "+" -> Op.Plus(l.toOpItem(), r.toOpItem())
    else -> exit()
  }
}

private class Monkey(
  val id: Int,
  private val _items: MutableList<Long>,
  private val op: Op,
  val testDivBy: Int,
  private val ifTrueId: Int,
  private val ifFalseId: Int,
) {
  var inspectedItemsCount: Long = 0
    private set

  fun processItems(monkeys: Map<Int, Monkey>, calcWorryLevel: (Long) -> Long) =
    _items.toList().also { _items.clear() }.forEach { worryLevel ->
      val newWorryLevel = calcWorryLevel(op(worryLevel))
      val throwTo = findThrowToMonkeyId(newWorryLevel)
      monkeys[throwTo]!!._items += newWorryLevel
      this.inspectedItemsCount += 1
    }

  fun findThrowToMonkeyId(worryLevel: Long): Int =
    if (worryLevel % testDivBy == 0L) ifTrueId else ifFalseId

  fun copy(): Monkey =
    Monkey(id, _items.toMutableList(), op, testDivBy, ifTrueId, ifFalseId)
}

private fun List<String>.toMonkey(): Monkey = Monkey(
  id = this[0].removePrefix("Monkey ").takeWhile { it.isDigit() }.toInt(),
  _items = this[1].removePrefix("  Starting items: ").split(", ").mapTo(ArrayList()) { it.toLong() },
  op = this[2].toOp(),
  testDivBy = this[3].removePrefix("  Test: divisible by ").toInt(),
  ifTrueId = this[4].removePrefix("    If true: throw to monkey ").toInt(),
  ifFalseId = this[5].removePrefix("    If false: throw to monkey ").toInt(),
)
