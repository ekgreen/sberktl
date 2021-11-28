package ru.sberschool.hystrix.pokemonapi

import mu.KLogging

class PokeApiFallback: PokeApi {

    override fun getPokemonByName(nameOfPokemon: String): Pokemon {
        logger.warn { "сервис поиска покемонов по имени - недоступен. так что мы дадим вам Слоупока" }
        return Pokemon(79, "slowpoke", 63, 12, 360, 120)
    }

    companion object: KLogging()
}