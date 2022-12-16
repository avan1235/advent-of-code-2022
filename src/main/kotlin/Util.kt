import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign
import kotlin.system.exitProcess

inline fun <reified T> String.value(): T = when (T::class) {
  String::class -> this as T
  Long::class -> toLongOrNull() as T
  Int::class -> toIntOrNull() as T
  else -> TODO("Add support to read ${T::class.java.simpleName}")
}

fun exit(code: Int = 1): Nothing = exitProcess(code)

inline fun <reified T> String.separated(by: String): List<T> = split(by).map { it.value() }

fun <T> T.printIt() = also { println(it) }

fun <U, V> List<U>.groupSeparatedBy(
  separator: (U) -> Boolean,
  includeSeparator: Boolean = false,
  transform: (List<U>) -> V,
): Sequence<V> = sequence {
  var curr = mutableListOf<U>()
  this@groupSeparatedBy.forEach {
    if (separator(it) && curr.isNotEmpty()) yield(transform(curr))
    if (separator(it)) curr = if (includeSeparator) mutableListOf(it) else mutableListOf()
    else curr += it
  }
  if (curr.isNotEmpty()) yield(transform(curr))
}

fun <T> List<List<T>>.transpose(): List<List<T>> {
  val n = map { it.size }.toSet().singleOrNull()
    ?: throw IllegalArgumentException("Invalid data to transpose: $this")
  return List(n) { y -> List(size) { x -> this[x][y] } }
}

infix fun Int.directedTo(o: Int) = if (this <= o) this..o else this downTo o

class DefaultMap<K, V>(
  private val default: V,
  private val map: MutableMap<K, V> = HashMap(),
) : MutableMap<K, V> by map {
  override fun get(key: K): V = map.getOrDefault(key, default).also { map[key] = it }
  operator fun plus(kv: Pair<K, V>): DefaultMap<K, V> = (map + kv).toDefaultMap(default)
  override fun toString() = map.toString()
  override fun hashCode() = map.hashCode()
  override fun equals(other: Any?) = map == other
  fun copy(copyKey: (K) -> K = { it }, copyValue: (V) -> V = { it }): DefaultMap<K, V> =
    DefaultMap<K, V>(default, HashMap()).also { result ->
      forEach { (k, v) -> result[copyKey(k)] = copyValue(v) }
    }
}

fun <K, V> Map<K, V>.toDefaultMap(default: V) = DefaultMap(default, toMutableMap())

class LazyDefaultMap<K, V>(
  private val default: () -> V,
  private val map: MutableMap<K, V> = HashMap(),
) : MutableMap<K, V> by map {
  override fun get(key: K): V = map.getOrDefault(key, default()).also { map[key] = it }
  operator fun plus(kv: Pair<K, V>): LazyDefaultMap<K, V> = (map + kv).toLazyDefaultMap(default)
  override fun toString() = map.toString()
  override fun hashCode() = map.hashCode()
  override fun equals(other: Any?) = map == other
  fun copy(copyKey: (K) -> K = { it }, copyValue: (V) -> V = { it }): LazyDefaultMap<K, V> =
    LazyDefaultMap<K, V>(default, HashMap()).also { result ->
      forEach { (k, v) -> result[copyKey(k)] = copyValue(v) }
    }
}

fun <K, V> Map<K, V>.toLazyDefaultMap(default: () -> V) = LazyDefaultMap(default, toMutableMap())

fun catchSystemOut(action: () -> Unit) = ByteArrayOutputStream().also {
  val originalOut = System.out
  System.setOut(PrintStream(it))
  action()
  System.setOut(originalOut)
}.toString()

inline fun <T> Sequence<T>.productOf(selector: (T) -> Int): Int {
  var product = 1
  for (element in this) {
    product *= selector(element)
  }
  return product
}

fun <K, V> Map<K, V>.copy(
  copyKey: (K) -> K = { it },
  copyValue: (V) -> V = { it },
): Map<K, V> =
  entries.associate { (k, v) -> copyKey(k) to copyValue(v) }

data class E<N>(val to: N, val w: Int)
class WeightedGraph<N>(
  private val adj: Map<N, List<E<N>>>,
) {

  fun reversed(): WeightedGraph<N> = WeightedGraph(adj = adj
    .flatMap { (s, adj) -> adj.map { d -> d.to to E(s, d.w) } }
    .groupBy(
      keySelector = { it.first },
      valueTransform = { it.second }
    )
  )

  fun shortestPathsLengths(source: N): DefaultMap<N, BigDecimal> {
    data class QN(val n: N, val dist: BigDecimal)

    val dist = DefaultMap<N, BigDecimal>(BigDecimal.ZERO)
    val queue = PriorityQueue<QN>(compareBy(selector = { it.dist }))
    adj.keys.forEach { v ->
      if (v != source) dist[v] = BigDecimal.valueOf(Long.MAX_VALUE)
      queue += QN(v, dist[v])
    }

    while (queue.isNotEmpty()) {
      val u = queue.remove()
      adj[u.n]?.forEach neigh@{ edge ->
        val alt = dist[u.n] + edge.w.toBigDecimal()
        if (alt >= dist[edge.to]) return@neigh
        dist[edge.to] = alt
        queue += QN(edge.to, alt)
      }
    }
    return dist
  }

  fun shortestPathLength(source: N, dest: N): BigDecimal =
    shortestPathsLengths(source)[dest]
}


typealias V2 = Pair<Int, Int>

operator fun V2.plus(v: V2): V2 = Pair(first + v.first, second + v.second)
operator fun V2.minus(v: V2): V2 = Pair(first - v.first, second - v.second)
val V2.length: Int get() = first * first + second * second
val V2.abs: V2 get() = V2(abs(first), abs(second))
val V2.normalized: V2 get() = V2(first.sign * min(1, abs(first)), second.sign * min(1, abs(second)))
