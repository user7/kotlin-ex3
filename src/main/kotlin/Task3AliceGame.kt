import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun main(args: Array<String>) {
    solveGame(AliceGameStdio())
    // testSolverAll()
}

interface AliceGame {
    fun maxStepsCount(): Int // returns max steps count, must be called first
    fun minDate(): Long
    fun maxDate(): Long
    fun probeDate(date: Long): Int
    fun tellAnswer(date: Long)
}

val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
val taskMinDate = dateParse("01.01.1970")
val taskMaxDate = dateParse("31.12.2020")
fun dateParse(s: String): Long = LocalDate.parse(s, formatter).toEpochDay()
fun dateFormat(date: Long): String = LocalDate.ofEpochDay(date).format(formatter)

class AliceGameStdio : AliceGame {
    override fun maxStepsCount(): Int = readLine()!!.toInt()
    override fun minDate() = taskMinDate
    override fun maxDate() = taskMaxDate

    override fun probeDate(date: Long): Int {
        println("? " + dateFormat(date))
        System.out.flush()
        val reply = readLine()!!
        return Regex("\\? ([0-9]+)").find(reply)!!.groupValues[1].toInt()
    }

    override fun tellAnswer(date: Long) {
        println("! " + dateFormat(date))
        System.out.flush()
        val reply = readLine()
        assert(reply != "!")
    }
}

fun solveGameStep(
    game: AliceGame,
    steps: Int,
    maxSteps: Int,
    minDate: Long,
    maxDate: Long,
    beforeMinDate: Long,
    count: Long,
    target: Long
): Int {
    val span = maxDate - minDate + 1
    if (span == count) {
        game.tellAnswer(minDate + target - 1)
        return steps
    }

    val minGuess = minDate + target - 1
    val maxGuess = maxDate - (count - target)
    val guess = (minGuess + maxGuess) / 2
    if (steps == maxSteps) {
        System.err.println("out of attempts but can't guess span=$span count=$count target=$target guess=$guess")
        game.tellAnswer(guess)
        return steps
    }

    val itemsToTheLeft = game.probeDate(guess) - beforeMinDate
    if (itemsToTheLeft >= target) {
        return solveGameStep(
            game = game,
            steps = steps + 1,
            maxSteps = maxSteps,
            minDate = minDate,
            maxDate = guess,
            beforeMinDate = beforeMinDate,
            count = itemsToTheLeft,
            target = target
        )
    } else {
        return solveGameStep(
            game = game,
            steps = steps + 1,
            maxSteps = maxSteps,
            minDate = guess + 1,
            maxDate = maxDate,
            beforeMinDate = beforeMinDate + itemsToTheLeft,
            count = count - itemsToTheLeft,
            target = target - itemsToTheLeft
        )
    }
}

fun solveGame(game: AliceGame): Int {
    val maxSteps = game.maxStepsCount()
    val count = game.probeDate(game.maxDate()).toLong()
    assert(count % 2 == 1L)
    return solveGameStep(
        game,
        1,
        maxSteps,
        game.minDate(),
        game.maxDate(),
        0,
        count,
        (count + 1) / 2
    )
}

//-------------------------

class AliceGameSim(
    private val maxSteps: Int,
    private val minDate: Long,
    private val maxDate: Long,
    datesList: List<Long>
) : AliceGame {

    private val dates = TreeMap<Long, Int>()

    init {
        var index = 1
        datesList.sorted().forEach {
            dates[it] = index
            index++
        }
    }

    override fun maxStepsCount() = maxSteps
    override fun minDate(): Long = minDate
    override fun maxDate(): Long = maxDate

    override fun probeDate(date: Long): Int {
        val ceil = dates.ceilingEntry(date) ?: return dates.size
        return if (ceil.key == date) ceil.value else ceil.value - 1
    }

    override fun tellAnswer(date: Long) {
        val answer = dates[date]
        assertNotEquals(null, answer, "bad guess date '$date', not in date set")
        assertEquals((dates.size + 1) / 2, answer, "bad guess date '$date', not a middle element")
        println("correct guess $date")
    }
}

fun testSolver(maxSteps: Int, minDate: Int, maxDate: Int, dates: List<Int>) {
    assertEquals(1, dates.size % 2)
    val correctAnswer = dates.sorted()[dates.size / 2]
    val testName = "$maxSteps range=[$minDate, $maxDate] dates=$dates answer=$correctAnswer"
    try {
        val steps = solveGame(AliceGameSim(maxSteps, minDate.toLong(), maxDate.toLong(), dates.map { it.toLong() }))
        println("ok $testName steps=$steps")
    } catch (e: Exception) {
        println("failed $testName: ${e.message}")
    }
}

fun testSolverAll() {
    testSolver(1, 100, 110, (100..110).toList())
//    testSolver(1, 100, 110, (100..108).toList())
    testSolver(2, 100, 110, (100..108).toList())

    // 1 of 1
    testSolver(1, 100, 100, listOf(100))

    // 1 of 2
    testSolver(2, 100, 101, listOf(100))
    testSolver(2, 100, 101, listOf(101))

    // 1 of 3
    testSolver(3, 100, 102, listOf(100))
    testSolver(3, 100, 102, listOf(101))
    testSolver(2, 100, 102, listOf(102))

    for (skip in 1..10) {
        testSolver(2, 101, 110, (1..10).toList().filter { it != skip }.map { it + 100 })
    }
}