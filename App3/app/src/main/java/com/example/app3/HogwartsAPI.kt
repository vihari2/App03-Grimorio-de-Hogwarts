package com.example.app3

import retrofit2.Response
import retrofit2.http.GET

interface HogwartsAPI {
    @GET("v1/characters")
    suspend fun getCharacters(): Response<CharacterListResponse>

    @GET("v1/creatures")
    suspend fun getCreatures(): Response<CreatureListResponse>
}
