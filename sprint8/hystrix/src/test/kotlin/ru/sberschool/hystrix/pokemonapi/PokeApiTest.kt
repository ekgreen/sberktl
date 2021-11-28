package ru.sberschool.hystrix.pokemonapi

import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class PokeApiTest {

    @Test
    fun `get pokemon by name`() {
        // given
        val client = createPokeApiClient(10, TimeUnit.SECONDS)

        // when
        val pokemon: Pokemon = client.getPokemonByName("pidgeot")

        // expect
        assertEquals("pidgeot", pokemon.name)
    }

    @Test
    fun `fallback scenario`() {
        // given
        val client = createPokeApiClient(1, TimeUnit.MILLISECONDS)

        // when
        val pokemon: Pokemon = client.getPokemonByName("pidgeot")

        // expect
        assertEquals("slowpoke", pokemon.name)
    }

    private fun createPokeApiClient(timeout: Long, unit: TimeUnit): PokeApi {
        return HystrixFeign.builder()
            .client(ApacheHttpClient())
            .decoder(JacksonDecoder())
            .options(Request.Options(timeout, unit, timeout, unit, true))
            .target(PokeApi::class.java, "https://pokeapi.co/api/v2", PokeApiFallback())
    }
}
