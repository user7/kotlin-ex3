fun String.splitIntsCh(n: Int) = this.split(" ").filter { it != "" }.map { it.toInt() }.chunked(n)
// fun String.toPoints() = this.splitIntsChunk(2).map { Pt(it[0], it[1]) }

fun main() {
    for (i in 1..0)
        println("i=$i")
}