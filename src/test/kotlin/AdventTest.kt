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
  )
}
