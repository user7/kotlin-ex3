import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    solveSums()
//    bruteForceTest(20, 100)
}

fun solveSums() {
    val wh = File("input.txt").readLines().first().split(" ").map { it.toInt() }
    val out = File("output.txt")
    out.writeText("")
    out.appendText(rowIntensitySumsLine(wh[0], wh[1]))
    out.appendText(rowIntensitySumsLine(wh[1], wh[0]))
}

fun rowIntensitySumsLine(width: Int, height: Int) =
    rowIntensitySums(width, height).joinToString(" ", "", "\n")

fun rowIntensitySums(width: Int, height: Int): List<Long> {
    var xsum: Long = 0
    (1..width).forEach { x -> xsum += x * (width.toLong() + 1 - x) }
    return (1..height).map { y -> y * (height.toLong() + 1 - y) * xsum }
}

class Matrix(val width: Int, val height: Int) {
    private val data = IntArray(width * height)
    fun get(x: Int, y: Int) = data[offset(x, y)]
    fun incRect(xb: Int, yb: Int, xe: Int, ye: Int) {
        for (x in xb..xe)
            for (y in yb..ye)
                inc(x, y)
    }
    private fun inc(x: Int, y: Int) { data[offset(x, y)]++ }
    private fun offset(x: Int, y: Int) = x - 1 + (y - 1) * width
}

fun bruteForceTest(n: Int, cutout: Int = 9999) {
    for (w in 1..n) {
        for (h in 1..n) {
            if (h * w > cutout)
                continue
            val m = Matrix(w, h)
            for (yb in 1..h)
                for (ye in yb..h)
                    for (xb in 1..w)
                        for (xe in xb..w)
                            m.incRect(xb, yb, xe, ye)
            val sums = rowIntensitySums(w, h)
            for(y in 1..h) {
                var sum: Long = 0
                (1..w).forEach { x -> sum += m.get(x, y) }
                assertEquals(sum, sums[y - 1],"check failed for matrix $w x $h at row $y")
            }
            println("$w x $h ok")
        }
    }
}