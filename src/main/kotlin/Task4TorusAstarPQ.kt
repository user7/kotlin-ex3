import java.io.File
import java.util.*

class Task4TorusAstarPQ(
    val sizey: Int,
    val sizex: Int,
    starty: Int,
    startx: Int,
    finishy: Int,
    finishx: Int
) {
    data class Pt(val x: Int, val y: Int)

    val linSize = sizex * sizey
    val start = Pt(startx, starty)
    val finish = Pt(finishx, finishy)

    fun off(pt: Pt) = pt.y * sizex + pt.x

    fun distCoord(c1: Int, c2: Int, modulo: Int): Int {
        val d = kotlin.math.abs(c1 - c2)
        return Integer.min(d, modulo - d)
    }

    fun dist(p1: Pt, p2: Pt) = distCoord(p1.x, p2.x, sizex) + distCoord(p1.y, p2.y, sizey)

    val curG = IntArray(linSize) { INF }
    val curF = IntArray(linSize) { 0 }
    val curPrev = CharArray(linSize) { ' ' }
    val que = TreeMap<Int, LinkedList<Pt>>()

    fun pushNode(pt: Pt) {
        val f = curF[off(pt)]
        if (!que.containsKey(f))
            que[f] = LinkedList<Pt>()
        que[f]!!.addFirst(pt)
    }

    fun popNode(): Pt? {
        while (true) {
            val e = que.firstEntry() ?: return null
            if (e.value.isEmpty()) {
                que.remove(e.key)
                continue
            }
            val ret = e.value.removeFirst()
            if (curF[off(ret)] != e.key)
                continue
            return ret
        }
    }

    fun wrap(c: Int, mod: Int) = (c + mod) % mod
    fun step(dir: Char, pos: Pt) =
        when (dir) {
            'W' -> Pt(wrap(pos.x - 1, sizex), pos.y)
            'E' -> Pt(wrap(pos.x + 1, sizex), pos.y)
            'N' -> Pt(pos.x, wrap(pos.y - 1, sizey))
            'S' -> Pt(pos.x, wrap(pos.y + 1, sizey))
            else -> TODO()
        }

    fun backTrack(pos: Pt): String {
        val revDir = mapOf('N' to 'S', 'S' to 'N', 'E' to 'W', 'W' to 'E')
        var ret = ""
        var cur = pos
        while (cur != start) {
            val dir = curPrev[off(cur)]
            ret += dir
            cur = step(revDir[dir]!!, cur)
        }
        return ret.reversed()
    }

    fun findPath(): String {
        if (start == finish)
            return ""
        curG[off(start)] = 0
        curF[off(start)] = dist(start, finish)
        pushNode(start)
        while (true) {
            val pos = popNode() ?: return "-1"
            val off = off(pos)
            val g2 = curG[off] + 1
            for (dir in listOf('E', 'S', 'W', 'N')) {
                val pos2 = step(dir, pos)
                val off2 = off(pos2)
                val g2old = curG[off2]
                if (g2 >= g2old)
                    continue
                curG[off2] = g2
                curF[off2] = dist(pos2, finish) + g2
                curPrev[off2] = dir
                if (pos2 == finish)
                    return backTrack(pos2)
                pushNode(pos2)
            }
        }
    }

    companion object {
        const val INF = Int.MAX_VALUE
        fun run() {
            val s = Scanner(File("input.txt"))
            val t = Task4TorusAstar(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt())
            for (i in 0 until t.linSize) {
                if (s.nextInt() == 1)
                    t.curG[i] = 0
            }
            val res = t.findPath()
            File("output.txt").writeText(res)
        }
    }
}

fun main() {
    Task4TorusAstarPQ.run()
}