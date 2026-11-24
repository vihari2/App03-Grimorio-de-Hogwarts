package com.example.app3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HogwartsAPI {
    @GET("api/characters")
    suspend fun getCharacters(
        @Query("search") search: String,
        @Query("page_size") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): Response<CharacterListResponse>

    @GET("api/creatures")
    suspend fun getCreatures(
        @Query("search") search: String,
        @Query("page_size") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): Response<CreatureListResponse>
}
