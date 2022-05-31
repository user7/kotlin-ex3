import java.io.File
import java.io.RandomAccessFile
import java.lang.RuntimeException
import java.util.*

class Task6TAF23 {
    fun checkFS(device: String) {
        val dev = RandomAccessFile(device, "r")
        assert(dev.length() < Int.MAX_VALUE)
        val len = dev.length().toInt()
        val mask = BitSet(len)
        var start = 0
        var bytesCount = 0

        // read single int
        fun readInt(off: Int): Int {
            if (off + 4 > len)
                throwBadBuffer()
            dev.seek(off.toLong())
            return Integer.reverseBytes(dev.readInt())
        }

        while (true) {
            val size = readInt(start)
            if (size < 0)
                throwBadBuffer()
            val next = readInt(start + size + 4)
            val end = start + size + 8
            if (!mask.get(start, end).isEmpty)
                throwBadBuffer()
            mask.set(start, end)
            bytesCount += end - start
            start = next
            if (next == 0)
                break
        }
        if (bytesCount != len)
            throw RuntimeException("data loss")
    }

    fun throwBadBuffer(): Nothing = throw RuntimeException("not a ring buffer")

    companion object {
        fun run() {
            val device = File("input.txt").readLines().first().trim()
            var msg = "OK"
            try {
                Task6TAF23().checkFS(device)
            } catch (e: RuntimeException) {
                msg = e.message.toString()
            }
            File("output.txt").writeText(msg)
            println(msg)
        }
    }
}

fun main() {
    Task6TAF23.run()
}