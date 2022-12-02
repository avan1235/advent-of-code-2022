object Day2 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val inputs = lines.map { it.split(" ") }
    inputs.sumOf { (op, me) -> op.toMove() vs me.toMove() }.printIt()
    inputs.sumOf { (op, me) -> op.toMove() by me.toStrategy() }.printIt()
  }
}

private enum class Move {
  ROCK, PAPER, SCISSORS
}

private enum class Strategy {
  LOOSE, DRAW, WIN
}

private val Move.points: Int get () = when (this) {
  Move.ROCK -> 1
  Move.PAPER -> 2
  Move.SCISSORS -> 3
}

private fun String.toMove(): Move = when (this) {
  "A" -> Move.ROCK
  "B" -> Move.PAPER
  "C" -> Move.SCISSORS
  "X" -> Move.ROCK
  "Y" -> Move.PAPER
  "Z" -> Move.SCISSORS
  else -> exit()
}

private fun String.toStrategy(): Strategy = when (this) {
  "X" -> Strategy.LOOSE
  "Y" -> Strategy.DRAW
  "Z" -> Strategy.WIN
  else -> exit()
}

private val Move.loosingOpponent: Move
  get() = when (this) {
    Move.ROCK -> Move.SCISSORS
    Move.PAPER -> Move.ROCK
    Move.SCISSORS -> Move.PAPER
  }

private val Move.winningOpponent: Move
  get() = when (this) {
    Move.ROCK -> Move.PAPER
    Move.PAPER -> Move.SCISSORS
    Move.SCISSORS -> Move.ROCK
  }

private infix fun Move.vs(me: Move): Int = me.points + when (me) {
  this -> 3
  this.loosingOpponent -> 0
  else -> 6
}

private infix fun Move.by(strategy: Strategy): Int = when (strategy) {
  Strategy.LOOSE -> this.loosingOpponent
  Strategy.DRAW -> this
  Strategy.WIN -> this.winningOpponent
}.let { me -> this vs me }

