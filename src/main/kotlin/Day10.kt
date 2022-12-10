import java.lang.StringBuilder

object Day10 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val instructions = lines.map { it.toInstruction() }

    CrtSimulation().apply {
      execute(instructions)
    }.let { crt ->
      crt.strengthsSum.printIt()
      crt.screen.printIt()
    }
  }
}

private class CrtSimulation {
  private var spritePosition = 1
  private var cycles = 0
  private var crtPosition = 0
  private val _screen = StringBuilder()
  val screen: String get() = _screen.toString()
  var strengthsSum = 0
    private set

  fun execute(instructions: List<Instruction>) {
    _screen.clear()
    instructions.forEach { instruction ->
      repeat(instruction.cycles) {
        cycles += 1

        val px = if (crtPosition in spritePosition - 1..spritePosition + 1) '#' else '.'
        _screen.append(px)

        crtPosition += 1
        if (crtPosition == 40) {
          crtPosition = 0
          _screen.appendLine()
        }

        if ((cycles - 20) % 40 == 0) {
          strengthsSum += cycles * spritePosition
        }
      }
      when (instruction) {
        is Instruction.AddX -> spritePosition += instruction.count
        Instruction.Noop -> Unit
      }
    }
  }
}

private fun String.toInstruction(): Instruction = when {
  startsWith("addx ") -> split(" ").let { (_, count) -> Instruction.AddX(count.toInt()) }
  this == "noop" -> Instruction.Noop
  else -> exit()
}

private sealed interface Instruction {
  val cycles: Int

  data class AddX(val count: Int) : Instruction {
    override val cycles: Int = 2
  }

  object Noop : Instruction {
    override val cycles: Int = 1
  }
}
