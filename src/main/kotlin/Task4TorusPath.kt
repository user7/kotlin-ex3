import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    torusTests()
    doTorus()
}

val S = 0
val E = 1
val N = 2
val W = 3
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
    val dirStep = arrayOf(sizex, 1, -sizex, -1)

    // data
    // val next = Array(4) { IntArray(linSize) { -1 } }
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

//    init {
//        // link first and last rows
//        val botBase = (sizey - 1) * sizex
//        for (x in 0 until sizex) {
//            next[N][x] = botBase + x
//            next[S][botBase + x] = x
//        }
//        // link first and last columns
//        for (y in 0 until sizey) {
//            val base = y * sizex
//            val baseEnd = base + sizex - 1
//            next[W][base] = baseEnd
//            next[E][baseEnd] = base
//        }
//    }
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
    doTorus()
    val got = File("output.txt").readText()
    if (exp != got) {
        val flatExp = exp.replace("\n", " ")
        val flatGot = got.replace("\n", " ")
        println("failed $name: expected '$flatExp', got '$flatGot'")
    } else {
        println("ok $name")
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
}