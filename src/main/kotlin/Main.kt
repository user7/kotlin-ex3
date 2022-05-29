import java.io.File
import kotlin.collections.HashMap
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    //fixStrings("input.txt", "output.txt")
    testAll()
}

fun fixStrings(inputFile: String, outputFile: String) {
    val pending = HashMap<Int, String>()
    var index = 1
    val numRegex = "[0-9]+".toRegex()
    val out = File(outputFile)
    out.writeText("")
    File(inputFile).forEachLine { line ->
        numRegex.find(line)?.let { match ->
            val num = line.substring(match.range).toInt()
            val fixedString = line.removeRange(match.range)
            pending[num] = fixedString
            while (true) {
                pending.remove(index)?.let {
                    out.appendText("$it\n")
                    index++
                } ?: break
            }
        }
    }
}

fun testFixStrings(input: List<String>, expected: List<String>) {
    File("in.txt").writeText(input.joinToString("\n"))
    fixStrings("in.txt", "out.txt")
    val output = File("out.txt").readLines()
    assertEquals(output, expected)
}

fun testAll() {
    testFixStrings(listOf("str3", "1smth", "bla2bla"), listOf("smth", "blabla", "str"))
    testFixStrings(listOf("str1", "smth2", "oxo3"), listOf("str", "smth", "oxo"))
    testFixStrings(listOf("1str", "2smth", "3oxo"), listOf("str", "smth", "oxo"))
    testFixStrings(listOf("ma1ha"), listOf("maha"))
    testFixStrings(listOf(), listOf())
}