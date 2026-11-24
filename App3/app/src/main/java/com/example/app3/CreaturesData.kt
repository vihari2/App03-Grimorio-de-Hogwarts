package com.example.app3

import com.google.gson.annotations.SerializedName

data class CreatureResponse(
    @SerializedName("data") val data: CreaturesData? = null
)

data class CreatureListResponse(
    @SerializedName("data") val data: List<CreaturesData>? = null
)

data class CreaturesData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("classification") val classification: String? = null,
    @SerializedName("native_to") val nativeTo: String? = null,
    @SerializedName("eye_color") val eyeColor: String? = null,
    @SerializedName("wingspan") val wingspan: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("related_to") val relatedTo: List<String>? = null
)

fun CreatureListResponse.getFirstCreature(): CreaturesData? {
    return data?.firstOrNull()
}

fun CreatureListResponse.getBestMatchCreature(query: String): CreaturesData? {
    val list = data ?: return null
    if (list.isEmpty()) return null
    val trimmedQuery = query.trim().lowercase()
    if (trimmedQuery.isEmpty()) return list.firstOrNull()

    list.firstOrNull { it.name?.lowercase() == trimmedQuery || it.slug?.lowercase() == trimmedQuery }?.let { return it }
    list.firstOrNull { it.name?.lowercase()?.startsWith(trimmedQuery) == true }?.let { return it }
    list.firstOrNull { it.name?.lowercase()?.contains(trimmedQuery) == true }?.let { return it }
    return list.firstOrNull()
}

fun CreatureResponse.toCreatureData(): CreaturesData? {
    return data
}
