import java.io.File
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    Task4TorusPath.run()
}

class Task4TorusPath {
    companion object {
        fun run() {
            val s = Scanner(File("input.txt"))
            val t = Torus(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt())
            for (pos in 0 until t.linSize) {
                if (s.nextInt() == 1)
                    t.isBlocked.set(pos)
            }
            File("output.txt").writeText(findPath(t))
        }
    }
}

const val S = 0
const val E = 1
const val N = 2
const val W = 3
val dirCode = arrayOf('S', 'E', 'N', 'W')

class Torus(
    sizey: Int,
    val sizex: Int,
    starty: Int,
    startx: Int,
    finishy: Int,
    finishx: Int
) {
    fun posFromCoords(y: Int, x: Int) = sizex * y + x
    val start = posFromCoords(starty, startx)
    val finish = posFromCoords(finishy, finishx)
    val linSize = sizex * sizey

    fun nextPos(dir: Int, pos: Int) =
        when (dir) {
            N -> if (pos >= sizex) pos - sizex else pos - sizex + linSize
            S -> if (pos < linSize - sizex) pos + sizex else pos + sizex - linSize
            E -> if (pos % sizex != sizex - 1) pos + 1 else pos + 1 - sizex
            W -> if (pos % sizex != 0) pos - 1 else pos - 1 + sizex
            else -> TODO()
        }

    val isBlocked = BitSet(linSize)
    val revPos = IntArray(linSize) { -1 }
    val revCode = CharArray(linSize) { ' ' }
}

fun findPath(t: Torus): String {
    if (t.start == t.finish)
        return ""
    t.isBlocked.set(t.start)
    t.isBlocked.set(t.finish, false)
    var wave = arrayListOf(t.start)
    var wave2 = ArrayList<Int>()
    while (wave.isNotEmpty()) {
        for (pos in wave) {
            for (dir in dirCode.indices) {
                val pos2 = t.nextPos(dir, pos)
                if (t.isBlocked.get(pos2))
                    continue
                t.isBlocked.set(pos2)
                t.revCode[pos2] = dirCode[dir]
                t.revPos[pos2] = pos
                if (pos2 == t.finish) {
                    val path = StringBuilder()
                    var cur = pos2
                    while (true) {
                        if (t.revPos[cur] == -1)
                            return path.toString().reversed()
                        path.append(t.revCode[cur])
                        cur = t.revPos[cur]
                    }
                }
                wave2.add(pos2)
            }
        }
        val tmp = wave
        wave = wave2
        wave2 = tmp
        wave2.clear()
    }
    return "-1"
}