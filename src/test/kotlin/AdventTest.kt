import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AdventTest {

  @Test
  fun `test days outputs`() {
    expectedOutputs.forEachIndexed { idx, expect ->
      val out = catchSystemOut { AdventDay.all[idx].solve() }
      assertEquals(expect, out)
    }
    println("Passed tests for ${expectedOutputs.size} days")
  }

  private val expectedOutputs = mutableListOf(
    "68787\n198041\n",
    "12679\n14470\n",
    "7990\n2602\n",
    "532\n854\n",
    "MQTPGLLDN\nLVZPSTTCZ\n",
    "1109\n3965\n",
    "1232307\n7268994\n",
    "1859\n332640\n",
    "6354\n2651\n",
    "13720\n" +
      "####.###..#..#.###..#..#.####..##..#..#.\n" +
      "#....#..#.#..#.#..#.#..#....#.#..#.#..#.\n" +
      "###..###..#..#.#..#.####...#..#....####.\n" +
      "#....#..#.#..#.###..#..#..#...#....#..#.\n" +
      "#....#..#.#..#.#.#..#..#.#....#..#.#..#.\n" +
      "#....###...##..#..#.#..#.####..##..#..#.\n\n",
  )
}
