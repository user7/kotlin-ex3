import java.io.File
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.collections.ArrayList

fun main() {
//    torusTests()
    doTorus()
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

fun doTorus() {
    val s = Scanner(File("input.txt"))
    val t = Torus(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt())
    for (pos in 0 until t.linSize) {
        if (s.nextInt() == 1)
            t.isBlocked.set(pos)
    }
    File("output.txt").writeText(findPath(t))
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

fun testInOut(name: String, input: String, expected: String) {
    val exp = expected.trimMargin()
    File("input.txt").writeText(input.trimMargin())
    val start = currentTimeMillis()
    doTorus()
    val time = (currentTimeMillis() - start) / 1000f
    val got = File("output.txt").readText()
    if (exp != got) {
        val flatExp = exp.replace("\n", " ")
        val flatGot = got.replace("\n", " ")
        println("failed $name: expected '$flatExp', got '$flatGot' time=$time")
    } else {
        println("ok $name time=$time")
    }
}

fun torusTests() {

    testInOut(
        name = "3x3 neighbor",
        input = """
            |3 3 0 0 1 0
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "S",
    )

    testInOut(
        name = "3x3 neighbor",
        input = """
            |3 3 0 0 2 0
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "N",
    )

    testInOut(
        name = "3x3 neighbor",
        input = """
            |3 3 0 0 0 1
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "E",
    )

    testInOut(
        name = "3x3 neighbor",
        input = """
            |3 3 0 0 0 2
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "W",
    )

    testInOut(
        name = "3x3 angle path",
        input = """
            |3 3 0 0 1 1
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "SE",
    )

    testInOut(
        name = "3x3 wrap angle path",
        input = """
            |3 3 0 0 2 1
            |0 0 0
            |0 0 0
            |0 0 0
        """,
        expected = "EN",
    )

    testInOut(
        name = "5x1 direct path",
        input = """
            |1 5 0 0 0 2
            |0 0 0 0 0
        """,
        expected = "EE",
    )

    testInOut(
        name = "5x1 blockage wrapped path",
        input = """
            |1 5 0 0 0 2
            |0 1 0 0 0
        """,
        expected = "WWW",
    )

    testInOut(
        name = "7x2 snake path",
        input = """
            |2 7 0 0 1 5
            |0 1 0 0 0 1 0
            |0 0 0 1 0 0 1
        """,
        expected = "SEESEESE",
    )

    testInOut(
        name = "yandex big",
        input = """
            |10 10
            |1 0 8 8
            |1 0 1 0 0 0 0 0 0 1
            |0 0 0 0 1 0 0 1 0 0
            |0 0 0 0 0 0 0 0 0 0
            |0 0 0 1 0 0 1 1 0 0
            |0 0 1 0 0 0 0 0 0 0
            |1 0 0 0 0 0 0 0 0 1
            |1 0 0 0 0 0 0 0 0 0
            |0 1 1 1 1 1 1 1 1 0
            |0 0 0 0 0 0 0 0 0 1
            |1 1 1 1 1 1 1 1 1 1
        """,
        expected = "SSSWWSSESESEEEEEEEE"
    )

    testInOut(
        name = "yandex blocked",
        input = """
            |4 4
            |0 3 2 1
            |1 1 1 0
            |1 1 1 1
            |1 0 1 1
            |1 1 1 1
        """,
        expected = "-1"
    )

    testInOut(
        name = "chain blockage",
        input = """
            |4 4
            |0 0 3 3
            |0 0 1 1
            |0 1 0 1
            |1 0 0 1
            |1 1 0 0
        """,
        expected = "-1"
    )

    for (n in 101..1001 step 100) {
        testInOut(
            name = "${n}x$n generated",
            input = "$n $n 0 0 ${n / 2} ${n / 2}" + " 0".repeat(n * n),
            expected = "S".repeat(n / 2) + "E".repeat(n / 2)
        )
    }

//    for (n in 101..1001 step 100) {
//        testInOut(
//            name = "${n}x$n impossible generated",
//            input = "$n $n 0 0 1 1"
//                    + " 0 1 0" + " 0".repeat(n - 3)
//                    + " 1 0 1" + " 0".repeat(n - 3)
//                    + " 0 1 0" + " 0".repeat(n - 3)
//                    + " 0".repeat(n * n - 3 * n),
//            expected = "-1"
//        )
//    }
}