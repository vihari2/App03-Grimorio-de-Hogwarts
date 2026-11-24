package com.example.app3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HogwartsAPI {
    @GET("v1/characters")
    suspend fun getCharacters(@Query("filter[name_cont]") name: String): Response<CharacterListResponse>

    @GET("v1/creatures")
    suspend fun getCreatures(@Query("filter[name_cont]") name: String): Response<CreatureListResponse>
}
