package com.example.app3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HogwartsAPI {
    @GET("v1/characters")
    suspend fun searchCharacters(@Query("filter[name_cont]") name: String): Response<CharacterListResponse>

    @GET("v1/creatures")
    suspend fun getCreatures(): Response<CreatureListResponse>
}
