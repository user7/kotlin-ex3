fun String.splitIntsCh(n: Int) = this.split(" ").filter { it != "" }.map { it.toInt() }.chunked(n)

fun main() {
    for (i in 1..0)
        println("i=$i")
}