import java.io.File
import java.util.*

class Task5Shortcut {
    class Node(var dir: String, var dist: Int)

    val path = ArrayList<Node>()

    fun process(dir: String, dist: Int) {
        if (dist == 0)
            return
        val noFlip = dir in listOf("TOP", "LEFT")
        val normDir = if (noFlip) dir else revDir[dir]!!
        val normDist = if (noFlip) dist else -dist
        if (path.isNotEmpty()) {
            val n = path.last()
            if (n.dir == normDir) {
                n.dist += normDist
                if (n.dist == 0)
                    path.removeAt(path.size - 1)
                return
            }
        }
        path.add(Node(normDir, normDist))
    }

    fun getPath(callback: (String, Int) -> Unit) {
        for (n in path) {
            callback(if (n.dist > 0) n.dir else revDir[n.dir]!!, kotlin.math.abs(n.dist))
        }
    }

    companion object {
        val revDir = mapOf("TOP" to "BOTTOM", "BOTTOM" to "TOP", "LEFT" to "RIGHT", "RIGHT" to "LEFT")

        fun run() {
            val pathFixer = Task5Shortcut()
            File("input.txt").forEachLine {
                val (dir, dist) = it.split(" ")
                pathFixer.process(dir, dist.toInt())
            }
            val out = File("output.txt")
            out.writeText("")
            pathFixer.getPath { dir, dist -> out.appendText("$dir $dist\n") }
        }
    }
}

fun main() {
    Task5Shortcut.run()
}