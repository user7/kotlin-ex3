import java.io.File
import java.util.*

class Task4TorusAstarPQ(
    val sizey: Int,
    val sizex: Int,
    starty: Int,
    startx: Int,
    val finishy: Int,
    val finishx: Int
) {
    inline fun packXY(x: Int, y: Int) = x.shl(16).or(y)
    val start = packXY(startx, starty)
    val finish = packXY(finishx, finishy)

    inline fun distCoordOrd(c1: Int, c2: Int, m: Int) = kotlin.math.min(c2 - c1, c1 + m - c2)
    inline fun distCoord(c1: Int, c2: Int, m: Int) = if (c1 < c2) distCoordOrd(c1, c2, m) else distCoordOrd(c2, c1, m)
    inline fun dist(x1: Int, y1: Int, x2: Int, y2: Int) = distCoord(x1, x2, sizex) + distCoord(y1, y2, sizey)

    val que = PriorityQueue<Long>()

    fun pushNode(pos: Int) {
        val f = getF(pos).toLong().shl(32)
        que.add(f or pos.toLong())
    }

    fun popNode(): Int {
        while (!que.isEmpty()) {
            val long = que.remove()
            val pos = long.and(0xFFFFFFFFL).toInt()
            val f = long.ushr(32).toInt()
            if (getF(pos) == f)
                return pos
        }
        return -1
    }

    val dirName = listOf('E', 'S', 'N', 'W')
    val maxX = (sizex - 1).shl(16)
    val maxY = sizey - 1
    inline fun step(dir: Int, pos: Int): Int =
        when (dir) {
            // 0=E, x++
            0 -> if (pos < maxX) pos + 0x10000 else pos.and(0xFFFF)
            // 2=W, x--
            3 -> if (pos >= 0x10000) pos - 0x10000 else pos + maxX
            // 1=S, y++
            1 -> if (posy(pos) == maxY) pos - maxY else pos + 1
            // 3=N, y--
            2 -> if (posy(pos) != 0) pos - 1 else pos + maxY
            else -> TODO()
        }

    fun backTrack(pos: Int): String {
        val sb = StringBuilder("")
        var cur = pos
        while (cur != start) {
            val dir = getDir(cur)
            sb.append(dirName[dir])
            cur = step(3 - dir /* reverse dir */, cur)
        }
        return sb.reversed().toString()
    }

    val data = Array(sizex) { IntArray(sizey * 3) { -1 } }

    // node: 8 bytes
    //  g:       Int
    //  h:       Short
    //  prevdir: Byte
    inline fun posx(pos: Int) = pos.ushr(16)
    inline fun posy(pos: Int) = pos.and(0xFFFF)

    inline fun setG(pos: Int, g: Int) { data[posx(pos)][posy(pos) * 3] = g   }
    inline fun getG(pos: Int) = data[posx(pos)][posy(pos) * 3]

    inline fun setDir(pos: Int, dir: Int) { data[posx(pos)][posy(pos) * 3 + 2] = dir }

    inline fun getDir(pos: Int) = data[posx(pos)][posy(pos) * 3 + 2]

    inline fun getH(pos: Int): Int {
        val x = posx(pos)
        val y = posy(pos)
        val yi = y * 3 + 1
        var h = data[x][yi]
        if (h == -1) {
            h = dist(x, y, finishx, finishy)
            data[x][yi] = h
        }
        return h
    }

    inline fun getF(pos: Int): Int = getG(pos) + getH(pos)

    fun findPath(): String {
        if (start == finish)
            return ""
        setG(start, 0)
        pushNode(start)
        while (true) {
            val pos = popNode()
            if (pos == -1)
                return "-1"
            val g2 = getG(pos)
            for (dir in 0..3) {
                val pos2 = step(dir, pos)
                val g2old = getG(pos2)
                if (g2old != -1 && g2 >= g2old)
                    continue
                setG(pos2, g2)
                setDir(pos2, dir)
                if (pos2 == finish)
                    return backTrack(pos2)
                pushNode(pos2)
            }
        }
    }

    companion object {
        fun run() {
            val s = Scanner(File("input.txt"))
            val t = Task4TorusAstarPQ(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt())
            for (y in 0 until t.sizey) {
                for (x in 0 until t.sizex) {
                    if (s.nextInt() == 1)
                        t.setG(t.packXY(x, y), 0)
                }
            }
            val res = t.findPath()
            File("output.txt").writeText(res)
            // println(res)
        }
    }
}

fun main() {
    Task4TorusAstarPQ.run()
}