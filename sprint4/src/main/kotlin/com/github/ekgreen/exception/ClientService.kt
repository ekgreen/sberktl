package com.github.ekgreen.exception

import mu.KLogging

class ClientService {

    fun saveClient(client: Client): Client = client
        .also { validateClient(client) }
        .let { saveToMyPhantomDB(client) }
        .also { logger.info { "Успешно сохранена новая версия $it" } }


    private fun validateClient(client: Client) {
        val errors = ClientValidator.validate(client)

        if (errors.isNotEmpty())
            throw ValidationException(*errors.toTypedArray())
    }

    private fun saveToMyPhantomDB(client: Client) = client
        .also { it.incrementVersion() }

    companion object : KLogging()
}