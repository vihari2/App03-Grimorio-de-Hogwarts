package com.example.app3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class CharacterActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var llResult: LinearLayout
    private lateinit var tvError: TextView
    private lateinit var tvName: TextView
    private lateinit var tvSpecies: TextView
    private lateinit var tvHouse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        
        // Portrait lock requested in spec can also be added via manifest, but we do it there.

        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        llResult = findViewById(R.id.llResult)
        tvError = findViewById(R.id.tvError)
        tvName = findViewById(R.id.tvName)
        tvSpecies = findViewById(R.id.tvSpecies)
        tvHouse = findViewById(R.id.tvHouse)

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            
            if (query.isEmpty()) {
                etSearch.error = "Por favor, digite um nome."
                return@setOnClickListener
            }
            if (query.length < 3) {
                etSearch.error = "A busca deve ter pelo menos 3 caracteres."
                return@setOnClickListener
            }
            
            etSearch.error = null
            buscarPersonagem(query)
        }
    }

    private fun buscarPersonagem(query: String) {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.hogwartsApi.getCharacters(query)
                withContext(Dispatchers.Main) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val character: CharactersData? = response.body()?.getFirstCharacter()
                        if (character != null) {
                            showCharacter(character)
                        } else {
                            showError("Nenhum personagem encontrado")
                        }
                    } else {
                        showError("Erro no servidor (código ${response.code()}). Tente novamente.")
                    }
                }
            } catch (e: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showError("Sem conexão com a internet. Tente novamente.")
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showError("Sem conexão com a internet. Tente novamente.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showError("Erro inesperado: ${e.localizedMessage ?: "Tente novamente."}")
                }
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        llResult.visibility = View.GONE
        tvError.visibility = View.GONE
        btnSearch.isEnabled = false
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        btnSearch.isEnabled = true
    }

    private fun showError(mensagem: String) {
        tvError.visibility = View.VISIBLE
        tvError.text = mensagem
    }

    private fun showCharacter(character: CharactersData) {
        llResult.visibility = View.VISIBLE
        tvName.text = "Nome: ${character.name ?: "Não informado"}"
        val species = if (character.species.isNullOrEmpty()) "Não informado" else character.species
        val house = if (character.house.isNullOrEmpty()) "Não informado" else character.house
        tvSpecies.text = "Espécie: $species"
        tvHouse.text = "Casa: $house"
    }
}