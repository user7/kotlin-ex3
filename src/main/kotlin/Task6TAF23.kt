import java.io.File
import java.io.RandomAccessFile
import java.lang.Integer.min
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class Task6TAF23 {
    fun checkFS(device: String) {
        val dev = RandomAccessFile(device, "r")
        assert(dev.length() < Int.MAX_VALUE)
        val len = dev.length().toInt()
        val mask = BitSet(len)
        var start = 0

        // read single int, allow short read
        fun readInt(off: Int): Int {
            if (off >= len)
                return 0 // throwBadBuffer()
            dev.seek(off.toLong())
            val bytes = ByteArray(4)
            dev.read(bytes, 0, min(len - off, 4))
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
        }

        while (true) {
            val size = readInt(start)
            if (size < 0)
                throwBadBuffer()
            val next = readInt(start + size + 4)
            val end = min(start + size + 8, len)
            if (!mask.get(start, end).isEmpty)
                throwBadBuffer()
            mask.set(start, end)
            start = next
            if (next == 0)
                break
        }
        if (mask.cardinality() != len)
            throw RuntimeException("data loss")
    }

    fun throwBadBuffer(): Nothing = throw RuntimeException("not a ring buffer")

    companion object {
        fun run() {
            val device = File("input.txt").readLines().first()
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