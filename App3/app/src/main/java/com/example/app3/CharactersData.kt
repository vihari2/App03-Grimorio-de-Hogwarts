package com.example.app3

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("data") val data: CharactersData? = null
)

data class CharacterListResponse(
    @SerializedName("data") val data: List<CharactersData>? = null
)

data class CharactersData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("house") val house: String? = null,
    @SerializedName("born") val born: String? = null,
    @SerializedName("died") val died: String? = null,
    @SerializedName("species") val species: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("blood_status") val bloodStatus: String? = null,
    @SerializedName("patronus") val patronus: String? = null,
    @SerializedName("boggart") val boggart: String? = null,
    @SerializedName("wands") val wands: List<String>? = null
)

fun CharacterListResponse.getFirstCharacter(): CharactersData? {
    return data?.firstOrNull()
}

fun CharacterListResponse.getBestMatchCharacter(query: String): CharactersData? {
    val list = data ?: return null
    if (list.isEmpty()) return null
    val trimmedQuery = query.trim().lowercase()
    if (trimmedQuery.isEmpty()) return list.firstOrNull()

    // 1. Exact match on name or slug
    list.firstOrNull { it.name?.lowercase() == trimmedQuery || it.slug?.lowercase() == trimmedQuery }?.let { return it }

    // 2. Name starts with query
    list.firstOrNull { it.name?.lowercase()?.startsWith(trimmedQuery) == true }?.let { return it }

    // 3. Name contains query
    list.firstOrNull { it.name?.lowercase()?.contains(trimmedQuery) == true }?.let { return it }

    // 4. Fallback to first
    return list.firstOrNull()
}

fun CharacterResponse.toCharacterData(): CharactersData? {
    return data
}
