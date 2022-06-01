import java.io.InputStream
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class TaskR1_SumContest {

    var n: Int = 0
    var rot: Int = 0
    var sum: Long = 0
    val data = ArrayList<Long>()
    var output: PrintStream? = null

    fun run(input: InputStream, output: PrintStream) {
        this.output = output
        val s = Scanner(input)
        n = s.nextInt()
        (1..n).forEach { addNum(s.nextLong()) }
        val q = s.nextInt()
        (1..q).forEach {
            val t = s.nextInt()
            if (t == 1) {
                val i = s.nextInt()
                val x = s.nextLong()
                replace(i, x)
            } else {
                rotate(s.nextInt())
            }
        }
    }

    fun addNum(v: Long) {
        data.add(v)
        sum += v
    }

    fun replace(i: Int, x: Long) {
        val j = (rot + i - 1) % n
        sum = sum - data[j] + x
        data[j] = x
        out()
    }

    fun rotate(k: Int) {
        rot = (rot + n - k) % n
        out()
    }

    fun out() = output!!.println(sum)
}

fun main() { TaskR1_SumContest().run(System.`in`, System.`out`) }