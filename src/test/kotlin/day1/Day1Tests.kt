package day1

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day1Tests {

    @Test
    fun `should only consider first and last digit`() {
        val testValue = "a1b2c3d4e5f"
        val expectedResult = 15
        assertEquals(expectedResult, cleanupCoordinates(testValue))
    }

    @Test
    fun `should return same digit twice if only 1 digit in string`() {
        val testValue = "treb7uchet"
        val expectedResult = 77
        assertEquals(expectedResult, cleanupCoordinates(testValue))
    }

    @Test
    fun `should calculate the sum of all input values`() {
        val testData = listOf("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet")
        val expectedResult = 142
        val actualResult = testData.sumOf { cleanupCoordinates(it) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should consider spelled out numbers as digits`() {
        val testValue = "4nineeightseven2"
        val expectedResult = 42
        val actualResult = cleanupCoordinates(transformWordsToDigits(testValue))
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun edgeCase() {
        val text = "x2twone3four"
        val regex = Regex("(?=(one|two|three|four|five|six|seven|eight|nine))")
        val findAll = regex.findAll(text)
        assertEquals("two", findAll.first().groupValues.last())
        assertEquals("four", findAll.last().groupValues.last())
    }

    @Test
    fun `should calculate the sum of all input values part 2`() {
        val testData = listOf(
            "two1nine",
            "eightwothree",
            "abcone2threexyz",
            "xtwone3four",
            "4nineeightseven2",
            "zoneight234",
            "7pqrstsixteen",
            "fivezg8jmf6hrxnhgxxttwoneg"
        )
        val expectedResult = 332
        val actualResult = testData
            .map { transformWordsToDigits(it) }
            .onEach { println(it) }
            .sumOf { cleanupCoordinates(it) }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun solution() {
        val inputFilePath = Paths.get("src/test/resources/data/day1/input.txt")
        assertTrue(inputFilePath.exists(), "Could not file input file with specified path")
        val result = Files.readAllLines(inputFilePath).sumOf { cleanupCoordinates(it) }
        println("Result is $result")
    }

    @Test
    fun `solution part 2`() {
        val inputFilePath = Paths.get("src/test/resources/data/day1/input.txt")
        assertTrue(inputFilePath.exists(), "Could not file input file with specified path")
        val result = Files.readAllLines(inputFilePath)
            .map { transformWordsToDigits(it) }
            .sumOf { cleanupCoordinates(it) }
        println("Result is $result")
    }
}

fun cleanupCoordinates(input: String): Int {
    val firstDigit = input.first { it.isDigit() }
    val lastDigit = input.last { it.isDigit() }
    return "$firstDigit$lastDigit".toInt()
}

fun transformWordsToDigits(input: String): String {
    val regex = Regex("(?=(one|two|three|four|five|six|seven|eight|nine))")
    var result = input

    val firstMatch = regex.findAll(result).firstOrNull()
    if (firstMatch != null) {
        val valueToReplace = firstMatch.groupValues.last()
        val value = englishNumbersToDigitsMap[valueToReplace].orEmpty()
        result = result.replaceRange(firstMatch.range, value)
    }

    val lastMatch = regex.findAll(result).lastOrNull()
    if (lastMatch != null) {
        val valueToReplace = lastMatch.groupValues.last()
        val value = englishNumbersToDigitsMap[valueToReplace].orEmpty()
        result = result.replaceRange(lastMatch.range, value)
    }
    return result;
}

val englishNumbersToDigitsMap = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)