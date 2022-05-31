import java.io.File

class InOutTestRunner {
    companion object {
        fun run(name: String, input: String, expected: String, runner: () -> Unit) {
            val exp = expected.trimMargin()
            File("input.txt").writeText(input.trimMargin())
            val start = System.currentTimeMillis()
            runner()
            val time = (System.currentTimeMillis() - start) / 1000f
            val got = File("output.txt").readText().trim()
            if (exp != got) {
                fun flattenShorten(s: String) = s.replace("\n", " ").let {
                    if (it.length <= 30) it else it.substring(0, 15) + "..." + it.takeLast(15)
                }

                val flatExp = flattenShorten(exp)
                val flatGot = flattenShorten(got)
                println("failed $name: expected '$flatExp', got '$flatGot' time=$time")
            } else {
                println("ok $name time=$time")
            }
        }
    }
}