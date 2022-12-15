import kotlin.math.absoluteValue

object Day15 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val sensors = lines.map { it.toSensor() }
    sensors.flatMap { it.notPossibleBeaconPositions(y = 2000000) }.toSet().count().printIt()
    sensors.findDistressBeacon()?.tuningFrequency.printIt()
  }
}

private val V2.x: Int get() = first
private val V2.y: Int get() = second


private val V2.tuningFrequency: Long get() = x * 4_000_000L + y

private fun String.toSensor(): Sensor {
  val (sensor, beacon) = removePrefix("Sensor at ").split(": closest beacon is at ")
  return Sensor(sensor.toSinglePosition(), beacon.toSinglePosition())
}

private fun String.toSinglePosition(): V2 = split(", ").let { (x, y) ->
  Pair(x.removePrefix("x=").toInt(), y.removePrefix("y=").toInt())
}

private infix fun V2.distance(o: V2): Int =
  (x - o.x).absoluteValue + (y - o.y).absoluteValue

private data class Sensor(
  val p: V2,
  val beacon: V2,
) {
  val d: Int = p distance beacon

  fun notPossibleBeaconPositions(y: Int): Set<V2> = sequence {
    val b0 = V2(p.first, y)
    if (b0 distance p <= d) yield(b0)
    for (i in 1..d) {
      val b = V2(p.first + i, y)
      if (b distance p > d) break
      yield(b)
      yield(V2(p.first - i, y))
    }
  }.filterNot { it == beacon }.toHashSet()

  infix fun distance(o: V2): Int = p distance o
}

private fun List<Sensor>.findDistressBeacon(): V2? {
  for (s1 in this) for (s2 in this) if (s1 != s2) {
    val d1s = listOf(s1.d + 1, -s1.d - 1)
    val d2s = listOf(s2.d + 1, -s2.d - 1)
    val xs = listOf(s1.p.x - s2.p.x, s2.p.x - s1.p.x)
    val ys = listOf(s1.p.y - s2.p.y, s2.p.y - s1.p.y)
    for (x1c in d1s) for (x2c in d2s) for (yd in ys)
      for (y1c in d1s) for (y2c in d2s) for (xd in xs) V2(
        (s1.p.x + x1c + s2.p.x + x2c + yd) / 2,
        (s1.p.y + y1c + s2.p.y + y2c + xd) / 2,
      ).takeIf { b ->
        all { it distance b > it.d } &&
          b.x in 0..4_000_000 &&
          b.y in 0..4_000_000
      }?.let { return it }
  }
  return null
}
