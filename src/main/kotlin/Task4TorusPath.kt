import java.io.File
import java.util.*

fun main(args: Array<String>) {
    doTorus()
}

data class XY(val x: Int, val y: Int)
data class Dir(val id: Int, val revId: Int, val dx: Int, val dy: Int, val code: Char)

val dirs = arrayOf(
    Dir(1, 3, 0, 1, 'N'),
    Dir(2, 1, 0, 'E'),
    Dir(3, 0, -1, 'S'),
    Dir(4, -1, 0, 'W'),
)
val cellBlocked = -1

class Torus(
    val size: XY,
    val start: XY,
    val finish: XY
) {
    val data = IntArray(size.x * size.y)
    fun setAt(at: XY, value: Int) = value.let { data[offset(at)] = it }
    fun getAt(at: XY): Int = data[offset(at)]
    fun step(at: XY, dir: Dir) = XY(wrap(at.x + dir.dx, size.x), wrap(at.y + dir.dy, size.y))

    fun offset(at: XY) = at.x * size.y + at.y
    fun wrap(pos: Int, maxPos: Int) = (pos + maxPos) % maxPos
}

fun doTorus() {
    val s = Scanner(File("input.txt"))
    fun readXY(): XY {
        val y = s.nextInt()
        val x = s.nextInt()
        return XY(x, y)
    }

    val size = readXY()
    val t = Torus(size, readXY(), readXY())
    for (x in 0 until size.x)
        for (y in 0 until size.y)
            if (s.nextInt() != 0)
                t.setAt(XY(x, y), cellBlocked)

    val out = findPath(t)
    File("output.txt").writeText(out ?: "-1")
}

fun findPath(t: Torus): String? {
    val wave = LinkedList<XY>()
    wave.add(t.start)
    val wave2 = LinkedList<XY>()
    while(true) {
        for (pt in wave) {
            for (dir in dirs) {
                var step = t.step(pt, dir)
                if (t.getAt(step) == 0) {
                    t.setAt(step, dir.id)
                    if (step == t.finish) {
                        var path = String()
                        while (step != t.start) {
                            val fDir = t.getAt(step)
                            path += dirs[fDir - 1]
                            step = t.step(step, dirs[(fDir - 1 + 2) % 2 + 1])
                        }

                    }
                    wave2.add(step)
                }
            }
        }
        if ()
    }
}
