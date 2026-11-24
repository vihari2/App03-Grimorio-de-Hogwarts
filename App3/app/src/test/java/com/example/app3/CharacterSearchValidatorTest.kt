package com.example.app3

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CharacterSearchValidatorTest {

    @Test
    fun testEmptyQueryReturnsError() {
        val result = CharacterSearchValidator.validateQuery("")
        assertNotNull("Query vazia deve retornar mensagem de erro", result)
    }

    @Test
    fun testOnlySpacesQueryReturnsError() {
        val result = CharacterSearchValidator.validateQuery("   ")
        assertNotNull("Query contendo apenas espaços deve retornar mensagem de erro", result)
    }

    @Test
    fun testTwoCharactersQueryReturnsError() {
        val result = CharacterSearchValidator.validateQuery("ab")
        assertNotNull("Query com 2 caracteres deve retornar erro de mínimo de 3 caracteres", result)
    }

    @Test
    fun testThreeCharactersQueryIsValid() {
        val result = CharacterSearchValidator.validateQuery("abc")
        assertNull("Query com 3 caracteres deve ser considerada válida (retornar null)", result)
    }

    @Test
    fun testSpacesAroundValidQueryIsValid() {
        val result = CharacterSearchValidator.validateQuery("  Weasley  ")
        assertNull("Query com texto válido e espaços nas pontas deve ser válida após o trim (retornar null)", result)
    }
}
