package ru.sberschool.hystrix.pokemonapi

import feign.Param
import feign.RequestLine

interface PokeApi {

    @RequestLine("GET /pokemon/{name}")
    fun getPokemonByName(@Param("name") nameOfPokemon: String): Pokemon
}