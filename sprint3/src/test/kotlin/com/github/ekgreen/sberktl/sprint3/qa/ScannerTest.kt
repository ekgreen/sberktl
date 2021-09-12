package com.github.ekgreen.sberktl.sprint3.qa

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import ru.sber.qa.ScanTimeoutException
import ru.sber.qa.Scanner
import kotlin.random.Random.Default

@TestInstance(PER_CLASS)
internal class ScannerTest {
    // given
    val scanner = spyk(Scanner)

    @BeforeAll
    fun beforeTest() {
        // чтобы не ждать
        every { scanner.sleep(any()) } returns Unit
    }

    @Test
    fun `getScanData() return byte array`(){
        // для каждого вызова будем возвращать значение меньше порогового на 1
        mockkObject(Default)
        every { Default.nextLong(5000, 15000) } returns Scanner.SCAN_TIMEOUT_THRESHOLD - 1

        // when
        val bytes = scanner.getScanData()

        // then
        Assertions.assertEquals(100, bytes.size)
    }

    @Test
    fun `getScanData() timeout cases`(){
        // given
        // для каждого вызова будем возвращать значение больше порогового на 1
        mockkStatic(Default::class)
        every { Default.nextLong(5000L, 15000L) } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        // then
        Assertions.assertThrows(ScanTimeoutException::class.java) {
            // when
            scanner.getScanData()
        }
    }

    @AfterAll
    fun afterTest(){
        unmockkAll()
    }
}


