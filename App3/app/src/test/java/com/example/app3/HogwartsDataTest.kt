package com.example.app3

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class HogwartsDataTest {

    private val gson = Gson()

    @Test
    fun testCharacterListGetFirstCharacter() {
        val json = """
            {
              "data": [
                {
                  "id": "harry-potter",
                  "type": "character",
                  "attributes": {
                    "slug": "harry-potter",
                    "name": "Harry Potter",
                    "house": "Gryffindor"
                  }
                },
                {
                  "id": "james-potter",
                  "type": "character",
                  "attributes": {
                    "slug": "james-potter",
                    "name": "James Potter",
                    "house": "Gryffindor"
                  }
                }
              ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, CharacterListResponse::class.java)
        val firstCharacter = response.getFirstCharacter()
        assertNotNull(firstCharacter)
        assertEquals("Harry Potter", firstCharacter?.name)
    }

    @Test
    fun testCreatureListGetFirstCreature() {
        val json = """
            {
              "data": [
                {
                  "id": "blast-ended-skrewt",
                  "type": "creature",
                  "attributes": {
                    "slug": "blast-ended-skrewt",
                    "name": "Blast-Ended Skrewt"
                  }
                }
              ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, CreatureListResponse::class.java)
        val firstCreature = response.getFirstCreature()
        assertNotNull(firstCreature)
        assertEquals("Blast-Ended Skrewt", firstCreature?.name)
    }
}
