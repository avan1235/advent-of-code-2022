import java.lang.StringBuilder
import kotlin.math.max

object Day13 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val packets = lines.groupSeparatedBy(
      separator = { it == "" },
      transform = { it }
    ).toList()

    packets.mapIndexedNotNull { idx, (fst, snd) ->
      idx.takeIf { PacketComparator.compare(fst, snd) == -1 }
    }
      .sumOf { it + 1 }
      .printIt()

    val dividers = listOf("[[2]]", "[[6]]")
    val allSorted = (packets.flatten() + dividers).sortedWith(PacketComparator)
    dividers
      .map { allSorted.indexOf(it) + 1 }
      .let { (fst, snd) -> fst * snd }
      .printIt()
  }
}

private object PacketComparator : Comparator<String> {
  override fun compare(l: String?, r: String?): Int {
    if (l == null && r == null) return 0
    if (l == null) return -1
    if (r == null) return 1

    if (l.isEmpty() && r.isEmpty()) return 0
    if (l.isEmpty()) return -1
    if (r.isEmpty()) return 1

    val digitsL = l.all { it.isDigit() }
    val digitsR = r.all { it.isDigit() }
    return when {
      digitsL && digitsR -> l.toInt().compareTo(r.toInt())
      digitsL -> compare("[$l]", r)
      digitsR -> compare(l, "[$r]")
      else -> {
        val listL = l.splitAsPacket()
        val listR = r.splitAsPacket()
        for (i in 0 until max(listL.size, listR.size)) {
          if (i !in listL.indices) return -1
          if (i !in listR.indices) return 1
          val curr = compare(listL[i], listR[i])
          if (curr != 0) return curr else continue
        }
        0
      }
    }
  }
}

private fun String.splitAsPacket(): List<String> {
  var needsMatch = 0
  val chars = toCharArray()
  val packets = mutableListOf<String>()
  val curr = StringBuilder()

  chars.forEach { c ->
    when (c) {
      '[' -> {
        if (needsMatch > 0) {
          curr.append(c)
        }
        needsMatch += 1
      }

      ']' -> {
        if (needsMatch == 1) {
          packets += curr.toString()
          curr.clear()
        } else if (needsMatch > 0) {
          curr.append(c)
        }
        needsMatch -= 1
      }

      ',' -> {
        if (needsMatch == 1) {
          packets += curr.toString()
          curr.clear()
        } else if (needsMatch > 0) {
          curr.append(c)
        }
      }

      else -> curr.append(c)
    }
  }
  return packets
}
