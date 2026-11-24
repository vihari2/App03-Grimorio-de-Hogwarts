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
    @SerializedName("native_to") val nativeTo: String? = null
)

fun CreatureListResponse.getFirstCreature(): CreaturesData? {
    return data?.firstOrNull()
}

fun CreatureResponse.toCreatureData(): CreaturesData? {
    return data
}
