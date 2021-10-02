package com.github.ekgreen.exception

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class ClientServiceTest {

    private val gson = Gson()
    private val clientService = ClientService()

    @Test
    fun `success save client`() {
        val client = getClientFromJson("/exception/success/user.json")
        val result = clientService.saveClient(client)
        assertNotNull(result)
    }

    @Test
    fun `fail save client - with bad phone number`() {
        val client = getClientFromJson("/exception/fail/user_with_bad_phone.json")
        val exception = assertThrows<ValidationException>("Данные мобильного телефона - испорчены") {
            clientService.saveClient(client)
        }
        assertEquals(exception.errorCode[0], ErrorCode.MOBILE_NUMBER_OUT_OF_RANGE)
        assertEquals(exception.errorCode[1], ErrorCode.NOT_RU_MOBILE_NUMBER)
    }

    @Test
    fun `fail save client - corrupted data`() {
        val client = getClientFromJson("/exception/fail/user_data_corrupted.json")
        val exception = assertFailsWith<ValidationException>("Данные по клиенту испорчены") {
            clientService.saveClient(client)
        }
        assertEquals(exception.errorCode[0], ErrorCode.LAST_NAME_OUT_OF_RANGE)
        assertEquals(exception.errorCode[1], ErrorCode.FIRST_NAME_IS_REQUIRED)
        assertEquals(exception.errorCode[2], ErrorCode.MOBILE_NUMBER_OUT_OF_RANGE)
        assertEquals(exception.errorCode[3], ErrorCode.NOT_RU_MOBILE_NUMBER)
        assertEquals(exception.errorCode[4], ErrorCode.EMAIL_NOT_VALID_REGEXP)
        assertEquals(exception.errorCode[5], ErrorCode.SNILS_CONTROL_SUM_NOT_MATCHED)
    }

    @Test
    fun `fail save client - foreign client`() {
        val client = getClientFromJson("/exception/fail/foreign_client.json")
        val exception = assertFailsWith<ValidationException>("Иностранный клиент: латинское имя, мобильный номер из США и нет СНИЛС") {
            clientService.saveClient(client)
        }
        assertEquals(exception.errorCode[0], ErrorCode.NOT_CYRILLIC_LAST_NAME)
        assertEquals(exception.errorCode[1], ErrorCode.NOT_CYRILLIC_FIRST_NAME)
        assertEquals(exception.errorCode[2], ErrorCode.NOT_RU_MOBILE_NUMBER)
        assertEquals(exception.errorCode[3], ErrorCode.SNILS_IS_REQUIRED)
    }

    @Test
    fun `fail save client - antiquity client`() {
        val client = getClientFromJson("/exception/fail/antiquity_client.json")
        val exception = assertFailsWith<ValidationException>("Клиент из античности: тройная фамилия, нет телефона и СНИЛС") {
            clientService.saveClient(client)
        }
        assertEquals(exception.errorCode[0], ErrorCode.LAST_NAME_OUT_OF_RANGE)
        assertEquals(exception.errorCode[1], ErrorCode.NOT_CYRILLIC_LAST_NAME)
        assertEquals(exception.errorCode[2], ErrorCode.PHONE_NUMBER_IS_REQUIRED)
        assertEquals(exception.errorCode[3], ErrorCode.EMAIL_NOT_VALID_REGEXP)
        assertEquals(exception.errorCode[4], ErrorCode.SNILS_IS_REQUIRED)
    }

    @Test
    fun `fail save client - empty client`() {
        val client = getClientFromJson("/exception/fail/missed_user_data.json")
        val exception = assertFailsWith<ValidationException>("Нет данных по клиенту") {
            clientService.saveClient(client)
        }
        assertEquals(exception.errorCode[0], ErrorCode.LAST_NAME_IS_REQUIRED)
        assertEquals(exception.errorCode[1], ErrorCode.FIRST_NAME_IS_REQUIRED)
        assertEquals(exception.errorCode[2], ErrorCode.PHONE_NUMBER_IS_REQUIRED)
        assertEquals(exception.errorCode[3], ErrorCode.EMAIL_IS_REQUIRED)
        assertEquals(exception.errorCode[4], ErrorCode.SNILS_IS_REQUIRED)
    }

    private fun getClientFromJson(fileName: String): Client = this::class.java.getResource(fileName)
        .takeIf { it != null }
        ?.let { gson.fromJson(it.readText(), Client::class.java) }
        ?: throw Exception("Что-то пошло не так))")

}