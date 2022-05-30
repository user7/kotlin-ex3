import java.io.File
import java.util.*

var inputFile = "input.txt"
var outputFile = "output.txt"
fun main(args: Array<String>) {
    testToruses()
}

data class Pt(val x: Int, val y: Int)

class Cell {
    var prev: Cell? = null
    var code: Char = ' '
}

class Torus(
    val size: Pt,
    val start: Pt,
    val finish: Pt
) {
    private val data = Array(size.x * size.y) { Cell() }

//    private val next = Array(4) { Array(size.x * size.y) { 0 }}
//
//    init {
//        for (x in 0 until size.x) {
//            for (y in 0 until size.y) {
//
//            }
//        }
//    }
//    private val nextS = Array()
//    private val nextS = Array()
//    fun at(abs: Int) = data[abs]

    fun at(at: Pt) = data[at.x * size.y + at.y]
}

fun doTorus() {
    val s = Scanner(File(inputFile))
    fun readPt() = s.nextInt().let { Pt(s.nextInt(), it) }
    val t = Torus(readPt(), readPt(), readPt())
    for (y in 0 until t.size.y)
        for (x in 0 until t.size.x)
            if (s.nextInt() == 1)
                t.at(Pt(x, y)).code = 'x'
    File(outputFile).writeText(findPath(t))
}

fun findPath(t: Torus): String {
    if (t.start == t.finish)
        return ""
    t.at(t.start).code = 'x'
    t.at(t.finish).code = ' '
    var wave = listOf(t.start)

    // step directions
    val dirCode = arrayOf('S', 'E', 'N', 'W')
    val dx = arrayOf(0, 1, 0, -1)
    val dy = arrayOf(1, 0, -1, 0)

    while (wave.isNotEmpty()) {
        val wave2 = LinkedList<Pt>()
        for (pt in wave) {
            val cell = t.at(pt)
            for (dir in dirCode.indices) {
                val pt2 = Pt((pt.x + dx[dir] + t.size.x) % t.size.x, (pt.y + dy[dir] + t.size.y) % t.size.y)
                val cell2 = t.at(pt2)
                if (cell2.code != ' ')
                    continue
                cell2.code = dirCode[dir]
                cell2.prev = cell
                if (pt2 == t.finish) {
                    var cur = cell2
                    var path = ""
                    while (true) {
                        val next = cur.prev ?: return path
                        path = cur.code + path
                        cur = next
                    }
                }
                wave2.add(pt2)
            }
        }
        wave = wave2
    }
    return "-1"
}

fun String.toPoints() = this.splitIntsCh(2).map { Pt(it[0], it[1]) }

fun testInOut(name: String, input: String, expected: String) {
    inputFile = "in.txt"
    outputFile = "out.txt"
    val exp = expected.trimMargin()
    File(inputFile).writeText(input.trimMargin())
    doTorus()
    val got = File(outputFile).readText()
    if (exp != got) {
        val flatExp = exp.replace("\n", " ")
        val flatGot = got.replace("\n", " ")
        println("failed $name: expected '$flatExp', got '$flatGot'")
    } else {
        println("ok $name")
    }
}

fun testToruses() {

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
        expected = "NE",
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