package com.github.ekgreen.sberktl.sprint3.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.sber.qa.CertificateRequest
import ru.sber.qa.CertificateType
import ru.sber.qa.Scanner

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CertificateRequestTest {

    @Test
    fun process() {
        // given
        val certificateRequest: CertificateRequest = CertificateRequest(EMPLOYEE_NUMBER, CertificateType.NDFL)
        val array: ByteArray = ByteArray(100)

        mockkObject(Scanner)
        every { Scanner.getScanData() } returns array

        // when
        val certificate = certificateRequest.process(EMPLOYEE_NUMBER)

        // then
        assertEquals(certificate.certificateRequest, certificateRequest)
        assertEquals(certificate.processedBy, EMPLOYEE_NUMBER)
        assertEquals(certificate.data, array)
    }

    companion object {
        const val EMPLOYEE_NUMBER: Long = 1L
    }

    @AfterAll
    fun afterTest(){
        unmockkAll()
    }
}