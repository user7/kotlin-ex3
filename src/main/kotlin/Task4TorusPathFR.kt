import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import java.io.File
import java.io.FileInputStream
import kotlin.collections.ArrayList

class Task4TorusPathFR {
    class Torus(
        val sizey: Int,
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

        fun posx(pos: Int) = pos % sizex
        fun posy(pos: Int) = pos / sizex
        fun wr(c: Int, mod: Int) = (c + mod) % mod
        val dirCodes = arrayOf('S', 'E', 'N', 'W')
        fun nextPos(dir: Int, pos: Int) =
            when (dir) {
                0 -> posFromCoords(wr(posy(pos) + 1, sizey), posx(pos))
                2 -> posFromCoords(wr(posy(pos) - 1, sizey), posx(pos))
                1 -> posFromCoords(posy(pos), wr(posx(pos) + 1, sizex))
                3 -> posFromCoords(posy(pos), wr(posx(pos) - 1, sizex))
                else -> TODO()
            }

        val revPos = IntArray(linSize) { -1 }
        val revCode = CharArray(linSize) { ' ' }

        fun block(pos: Int) {
            revCode[pos] = 'x'
        }

        fun unblock(pos: Int) {
            revCode[pos] = ' '
        }

        fun findPath(): String {
            if (start == finish)
                return ""
            block(start)
            unblock(finish)
            var wave = arrayListOf(start)
            var wave2 = ArrayList<Int>()
            while (wave.isNotEmpty()) {
                for (pos in wave) {
                    for (dir in dirCodes.indices) {
                        val pos2 = nextPos(dir, pos)
                        if (revCode[pos2] != ' ')
                            continue
                        revCode[pos2] = dirCodes[dir]
                        revPos[pos2] = pos
                        if (pos2 == finish) {
                            val path = StringBuilder()
                            var cur = pos2
                            while (true) {
                                if (cur == start)
                                    return path.reversed().toString()
                                path.append(revCode[cur])
                                cur = revPos[cur]
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
    }

    class InputScanner {
        val br = BufferedReader(InputStreamReader(FileInputStream("input.txt")))
        var st: StringTokenizer? = null
        operator fun next(): String {
            while (st?.hasMoreElements() != true) {
                st = StringTokenizer(br.readLine())
            }
            return st!!.nextToken()
        }

        fun int() = next().toInt()
        fun long() = next().toLong()
        fun double() = next().toDouble()
        fun line(): String {
            st = null
            return br.readLine()
        }
    }

    companion object {
        fun run() {
            val s = InputScanner()
            val t = Torus(s.int(), s.int(), s.int(), s.int(), s.int(), s.int())
            (0 until t.linSize).forEach { if (s.int() == 1) t.block(it) }
            File("output.txt").writeText(t.findPath())
        }
    }
}

fun main() {
    Task4TorusPathFR.run()
}