import java.util.Objects
import kotlin.math.max

object Day7 : AdventDay() {
  override fun solve() {
    val lines = getInputLines() ?: exit()
    val cmds = lines.groupSeparatedBy(
      separator = { it.startsWith("$") },
      includeSeparator = true,
      transform = { it.toCmd() }
    )
    val rootDir = cmds.asIterable().toFilesystemTree()
    val allDirs = buildSet { rootDir.search { item -> if (item is FilesystemItem.Dir) add(item) } }

    allDirs.filter { it.size <= 100000 }.sumOf { it.size }.printIt()

    val freeSpace = (70_000_000 - rootDir.size).also { if (it < 0) exit() }
    val needExtraSpace = max(0, 30_000_000 - freeSpace)
    allDirs.sortedBy { it.size }.first { it.size >= needExtraSpace }.size.printIt()
  }
}

private fun Iterable<Cmd>.toFilesystemTree(): FilesystemItem.Dir {
  var rootDir: FilesystemItem.Dir? = null

  fold(null as FilesystemItem.Dir?) { parentDir, cmd ->
    when (cmd) {
      is Cmd.Cd -> when (cmd.dirName) {
        ".." -> parentDir!!.parent
        else -> FilesystemItem.Dir(cmd.dirName, parentDir).also {
          if (it.parent == null) rootDir = it else it.parent += it
        }
      }

      is Cmd.Ls -> cmd.listed.forEach { item ->
        when (item) {
          is ListedItem.Dir -> Unit
          is ListedItem.File -> parentDir!! += FilesystemItem.File(item.name, item.size, parentDir)
        }
      }.let { parentDir }
    }
  }

  return rootDir!!
}

private fun List<String>.toCmd(): Cmd {
  val cmd = this.first()
  return when {
    cmd.startsWith("""$ cd""") ->
      Cmd.Cd(cmd.split(" ").let { (_, _, name) -> name })

    cmd.startsWith("""$ ls""") -> this.drop(1).map {
      it.split(" ").let { (data, name) ->
        when {
          data == "dir" -> ListedItem.Dir(name)
          data.all { c -> c.isDigit() } -> ListedItem.File(name, data.toInt())
          else -> exit()
        }
      }
    }.let { Cmd.Ls(it) }

    else -> exit()
  }
}

private sealed interface FilesystemItem {
  val name: String
  val parent: Dir?
  val size: Int

  class File(
    override val name: String,
    override val size: Int,
    override val parent: Dir?,
  ) : FilesystemItem {

    private val absolutePathHashCode: Int = Objects.hash(name, parent)

    override fun toString(): String =
      "File(name='$name', size=$size)"

    override fun equals(other: Any?): Boolean =
      (other as? File)?.let { it.name == name && it.parent == parent } ?: false

    override fun hashCode(): Int = absolutePathHashCode
  }

  class Dir(
    override val name: String,
    override val parent: Dir?,
  ) : FilesystemItem {
    private val _items: MutableMap<String, FilesystemItem> = hashMapOf()
    private var _size: Int = 0

    private val absolutePath: List<String> = parent?.absolutePath.orEmpty() + name
    private val absolutePathHashCode: Int = absolutePath.hashCode()

    val items: Set<FilesystemItem> get() = _items.values.toSet()
    override val size: Int get() = _size

    operator fun plusAssign(item: FilesystemItem) {
      _items[item.name] = item
      increaseSize(item.size)
    }

    private fun increaseSize(by: Int) {
      _size += by
      parent?.increaseSize(by)
    }

    override fun toString(): String =
      "Dir(name='$name', size=$size, items=${items.map { it.toString() }})"

    override fun equals(other: Any?): Boolean =
      (other as? Dir)?.absolutePath == this.absolutePath

    override fun hashCode(): Int = absolutePathHashCode
  }

  enum class SearchType { DFS, BFS }

  fun search(
    type: SearchType = SearchType.DFS,
    action: (FilesystemItem) -> Unit = {},
  ): Set<FilesystemItem> {
    val visited = mutableSetOf<FilesystemItem>()
    val queue = ArrayDeque<FilesystemItem>()
    tailrec fun go(curr: FilesystemItem) {
      visited += curr.also(action)
      when (curr) {
        is Dir -> curr.items
        is File -> emptyList()
      }
        .filterNot { it in visited }
        .forEach { queue += it }

      when (type) {
        SearchType.DFS -> go(queue.removeLastOrNull() ?: return)
        SearchType.BFS -> go(queue.removeFirstOrNull() ?: return)
      }
    }
    return visited.also { go(this) }
  }
}

private sealed interface Cmd {
  data class Cd(val dirName: String) : Cmd
  data class Ls(val listed: List<ListedItem>) : Cmd
}

private sealed interface ListedItem {
  data class File(val name: String, val size: Int) : ListedItem
  data class Dir(val name: String) : ListedItem
}

