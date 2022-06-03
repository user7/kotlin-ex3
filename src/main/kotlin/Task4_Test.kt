class Task4_Test(val runner: () -> Unit) {

    fun testAll(times: Int = 1) {
        val start = System.currentTimeMillis()
        repeat(times) { testAllAux() }
        val time = (System.currentTimeMillis() - start) / 1000f
        println("done, total time: $time")
    }

    fun test(name: String, input: String, expected: String) {
        InOutTestRunner.run(name, input, expected, runner)
    }

    fun testAllAux() {
        test(
            name = "3x3 neighbor S",
            input = """
        |3 3 0 0 1 0
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "S",
        )

        test(
            name = "3x3 neighbor N",
            input = """
        |3 3 0 0 2 0
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "N",
        )

        test(
            name = "3x3 neighbor E",
            input = """
        |3 3 0 0 0 1
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "E",
        )

        test(
            name = "3x3 neighbor W",
            input = """
        |3 3 0 0 0 2
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "W",
        )

        test(
            name = "3x3 angle path",
            input = """
        |3 3 0 0 1 1
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "SE",
        )

        test(
            name = "3x3 wrap angle path",
            input = """
        |3 3 0 0 2 1
        |0 0 0
        |0 0 0
        |0 0 0
    """,
            expected = "EN",
        )

        test(
            name = "5x1 direct path",
            input = """
        |1 5 0 0 0 2
        |0 0 0 0 0
    """,
            expected = "EE",
        )

        test(
            name = "5x1 blockage wrapped path",
            input = """
        |1 5 0 0 0 2
        |0 1 0 0 0
    """,
            expected = "WWW",
        )

        test(
            name = "7x2 snake path",
            input = """
        |2 7 0 0 1 5
        |0 1 0 0 0 1 0
        |0 0 0 1 0 0 1
    """,
            expected = "SEESEESE",
        )

        test(
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

        test(
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

        test(
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
            test(
                name = "${n}x$n generated",
                input = "$n $n 0 0 ${n / 2} ${n / 2}" + " 0".repeat(n * n),
                expected = "S".repeat(n / 2) + "E".repeat(n / 2)
            )
        }

        for (n in 101..1001 step 100) {
            test(
                name = "${n}x$n impossible generated",
                input = "$n $n 0 0 1 1"
                        + " 0 1 0" + " 0".repeat(n - 3)
                        + " 1 0 1" + " 0".repeat(n - 3)
                        + " 0 1 0" + " 0".repeat(n - 3)
                        + " 0".repeat(n * n - 3 * n),
                expected = "-1"
            )
        }
    }

    companion object {
        fun run(runner: () -> Unit) {
            readLine()
            Task4_Test(runner).testAll()
        }
    }
}