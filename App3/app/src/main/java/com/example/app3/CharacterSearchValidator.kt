package com.example.app3

object CharacterSearchValidator {
    fun validateQuery(query: String?): String? {
        val trimmed = query?.trim().orEmpty()
        if (trimmed.isEmpty()) {
            return "Digite o nome do personagem"
        }
        if (trimmed.length < 3) {
            return "Digite pelo menos 3 caracteres para buscar"
        }
        return null
    }
}
