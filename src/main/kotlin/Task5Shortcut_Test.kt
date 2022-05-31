fun main() {
    val runner = { Task5Shortcut.run() }

    InOutTestRunner.run(
        name = "annihilation TB",
        input = """
LEFT 10
TOP 50
BOTTOM 50
TOP 10
        """,
        expected = """
LEFT 10
TOP 10
        """,
        runner = runner
    )

    InOutTestRunner.run(
        name = "compensation with flip RL",
        input = """
TOP 40
RIGHT 10
LEFT 30
BOTTOM 40
        """,
        expected = """
TOP 40
LEFT 20
BOTTOM 40
        """,
        runner = runner
    )

    InOutTestRunner.run(
        name = "annihilation LTBR",
        input = """
TOP 42
LEFT 450
TOP 150
BOTTOM 150
RIGHT 450
LEFT 42
        """,
        expected = """
TOP 42
LEFT 42
        """,
        runner = runner
    )

    InOutTestRunner.run(
        name = "total annihilation",
        input = """
TOP 42
LEFT 450
TOP 150
BOTTOM 150
RIGHT 450
BOTTOM 42
        """,
        expected = "",
        runner = runner
    )

    InOutTestRunner.run(
        name = "accumulation",
        input = """
TOP 100
TOP 100
        """,
        expected = """
TOP 200
        """,
        runner = runner
    )

    InOutTestRunner.run(
        name = "multiple accumulation",
        input = """
TOP 100
TOP 100
BOTTOM 150
BOTTOM 150
        """,
        expected = """
BOTTOM 100
        """,
        runner = runner
    )

}