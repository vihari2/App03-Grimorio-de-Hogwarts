package com.example.app3

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("data") val data: CharacterItem? = null
)

data class CharacterListResponse(
    @SerializedName("data") val data: List<CharacterItem>? = null
)

data class CharacterItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("attributes") val attributes: CharactersData? = null
)

data class CharactersData(
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("house") val house: String? = null,
    @SerializedName("born") val born: String? = null,
    @SerializedName("died") val died: String? = null,
    @SerializedName("species") val species: String? = null,
    @SerializedName("patronus") val patronus: String? = null
)

fun CharacterListResponse.getFirstCharacter(): CharactersData? {
    return data?.firstOrNull()?.attributes
}

fun CharacterResponse.toCharacterData(): CharactersData? {
    return data?.attributes
}
