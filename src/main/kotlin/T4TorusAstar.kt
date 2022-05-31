import java.lang.Integer.min
import java.util.*

class T4TorusAstar(
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

    fun distCoord(c1: Int, c2: Int, modulo: Int): Int =
        if (c1 < c2) min(c2 - c1, c1 + modulo - c2) else distCoord(c2, c1, modulo)

    fun dist(p1: Pt, p2: Pt) = distCoord(p1.x, p2.x, sizex) + distCoord(p1.y, p2.y, sizey)

    val isBlocked = BitSet(linSize)
    val curG = IntArray(linSize) { INF }
    val curF = IntArray(linSize) { INF }
    val curPrev = CharArray(linSize) { ' ' }
    val que = TreeMap<Int, LinkedList<Pt>>()

    fun pushNode(pt: Pt, g: Int) {
        val f = dist(pt, finish) + g
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
            'N' -> Pt(wrap(pos.x - 1, sizex), pos.y)
            'S' -> Pt(wrap(pos.x + 1, sizex), pos.y)
            'E' -> Pt(pos.x, wrap(pos.y - 1, sizey))
            'W' -> Pt(pos.x, wrap(pos.y + 1, sizey))
            else -> TODO()
        }
    fun backTrack(pos: Pt): String {
        val revDir = mapOf('N' to 'S', 'S' to 'N', 'E' to 'W', 'W' to 'E')
        var ret = ""
        var cur = pos
        while (cur != start) {
            val dir = curPrev[off(pos)]
            ret += dir
            cur = step(revDir[dir]!!, cur)
        }
        return ret.reversed()
    }

    fun findPath(): String {
        if (start == finish)
            return ""
        curG[off(start)] = 0
        pushNode(start, 0)
        while (true) {
            val pos = popNode() ?: return "-1"
            val off = off(pos)
            val g2 = curG[off] + 1
            for (dir in listOf('S', 'E', 'N', 'W')) {
                val pos2 = step(dir, pos)
                val off2 = off(pos2)
                val g2old = curG[off2]
                if (g2 >= g2old)
                    continue
                curG[off2] = g2
                curPrev[off2] = dir
                if (pos2 == finish)
                    return backTrack(pos2)
                pushNode(pos2, g2)
            }
        }
    }

    companion object {
        const val INF = Int.MAX_VALUE
        const val NONE = -1
    }
}