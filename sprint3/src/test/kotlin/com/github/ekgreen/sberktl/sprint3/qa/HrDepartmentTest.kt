package com.github.ekgreen.sberktl.sprint3.qa

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.sber.qa.*
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HrDepartmentTest {

    // PS я понимаю что можно обращаться по HrDepartment.[метод]
    // не очень понятно, почему у нас HrDepartment object
    private val department: HrDepartment = HrDepartment

    // fields
    private lateinit var incomeBox: LinkedList<CertificateRequest>
    private lateinit var outcomeOutcome: LinkedList<Certificate>


    @BeforeEach
    fun setUp() {
        incomeBox = getQueueField("incomeBox")
        incomeBox.clear()

        outcomeOutcome = getQueueField("outcomeOutcome")
        outcomeOutcome.clear()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `receiveRequest() NDFL success push`(dayOfWeek: DayOfWeek) {
        // given
        val request = prepareReceiveRequest(dayOfWeek, CertificateType.NDFL)

        // when
        department.receiveRequest(request)

        // then
        Assertions.assertEquals(1, incomeBox.size)
        Assertions.assertEquals(request, incomeBox.first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `receiveRequest() LABOUR_BOOK success push`(dayOfWeek: DayOfWeek) {
        // given
        val request = prepareReceiveRequest(dayOfWeek, CertificateType.LABOUR_BOOK)

        // when
        department.receiveRequest(request)

        // then
        Assertions.assertEquals(1, incomeBox.size)
        Assertions.assertEquals(request, incomeBox.first)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["TUESDAY", "THURSDAY"])
    fun `receiveRequest() NDFL not allowed days`(dayOfWeek: DayOfWeek) {
        // given
        val request = prepareReceiveRequest(dayOfWeek, CertificateType.NDFL)

        // then
        Assertions.assertThrows(NotAllowReceiveRequestException::class.java) {
            // when
            department.receiveRequest(request)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun `receiveRequest() LABOUR_BOOK not allowed days`(dayOfWeek: DayOfWeek) {
        // given
        val request = prepareReceiveRequest(dayOfWeek, CertificateType.LABOUR_BOOK)

        // then
        Assertions.assertThrows(NotAllowReceiveRequestException::class.java) {
            // when
            department.receiveRequest(request)
        }
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["SUNDAY", "SATURDAY"])
    fun `receiveRequest() not allowed days`(dayOfWeek: DayOfWeek) {
        // given
        val request = prepareReceiveRequest(dayOfWeek, CertificateType.LABOUR_BOOK)

        // then
        Assertions.assertThrows(WeekendDayException::class.java) {
            // when
            department.receiveRequest(request)
        }
    }

    private fun prepareReceiveRequest(dayOfWeek: DayOfWeek, certificateType: CertificateType): CertificateRequest{
        val request: CertificateRequest = mockk<CertificateRequest>()

        // 1. Установим часы на нужный день недели ( в который возможно оформлять НДФЛ )
        mockkStatic(LocalDateTime::class)
        // простой способ задать дни недели ( или не простой )
        every { LocalDateTime.now(any<Clock>()) } returns LocalDateTime.of(1970, Month.JANUARY, 4 + dayOfWeek.value, 0, 0, 0)
        // 2. Установим тип справки - НДФЛ
        every { request.certificateType } returns certificateType

        return request
    }

    @Test
    fun processNextRequest() {
        // given
        val request: CertificateRequest = mockk<CertificateRequest>()
        val certificate: Certificate = mockk<Certificate>()

        incomeBox.add(request)
        every { request.process(any<Long>()) } returns certificate

        //  when
        department.processNextRequest(1)

        // then
        verify(exactly = 1) { request.process(any<Long>()) }

        Assertions.assertEquals(0, incomeBox.size)
        Assertions.assertEquals(1, outcomeOutcome.size)
        Assertions.assertEquals(certificate, outcomeOutcome.first)
    }

    private fun <T> getQueueField(name: String):  LinkedList<T> {
        val declaredField = department.javaClass.getDeclaredField(name)
        declaredField.trySetAccessible()
        return declaredField.get(department) as LinkedList<T>;
    }

    @AfterAll
    fun afterTest(){
        unmockkAll()
    }
}