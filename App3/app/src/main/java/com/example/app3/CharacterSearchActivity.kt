package com.example.app3

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class CharacterSearchActivity : AppCompatActivity() {

    private lateinit var etSearchName: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutDetails: LinearLayout
    private lateinit var tvCharacterName: TextView
    private lateinit var tvCharacterSpecies: TextView
    private lateinit var tvCharacterHouse: TextView
    private lateinit var tvStatusMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val paddingPx = (20 * resources.displayMetrics.density).toInt()
            v.setPadding(
                systemBars.left + paddingPx,
                systemBars.top + paddingPx,
                systemBars.right + paddingPx,
                systemBars.bottom + paddingPx,
            )
            insets
        }

        etSearchName = findViewById(R.id.etSearchName)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        layoutDetails = findViewById(R.id.layoutDetails)
        tvCharacterName = findViewById(R.id.tvCharacterName)
        tvCharacterSpecies = findViewById(R.id.tvCharacterSpecies)
        tvCharacterHouse = findViewById(R.id.tvCharacterHouse)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)

        btnSearch.setOnClickListener {
            executarBusca()
        }

        etSearchName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                executarBusca()
                true
            } else {
                false
            }
        }
    }

    private fun executarBusca() {
        etSearchName.error = null
        val rawInput = etSearchName.text?.toString()

        val error = CharacterSearchValidator.validateQuery(rawInput)
        if (error != null) {
            etSearchName.error = error
            return
        }

        val trimmedQuery = rawInput!!.trim()
        hideKeyboard()
        iniciarCarregamento()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.hogwartsApi.searchCharacters(trimmedQuery)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val character: CharactersData? = response.body()?.getFirstCharacter()
                        if (character != null) {
                            exibirDetalhes(character)
                        } else {
                            exibirMensagem("Nenhum personagem encontrado")
                        }
                    } else {
                        exibirMensagem("Erro no servidor (código ${response.code()}). Tente novamente.")
                    }
                }
            } catch (_: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    exibirMensagem("Sem conexão com a internet. Tente novamente.")
                }
            } catch (_: IOException) {
                withContext(Dispatchers.Main) {
                    exibirMensagem("Sem conexão com a internet. Tente novamente.")
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    exibirMensagem("Erro ao carregar dados do servidor. Tente novamente.")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finalizarCarregamento()
                }
            }
        }
    }

    private fun iniciarCarregamento() {
        progressBar.visibility = View.VISIBLE
        btnSearch.isEnabled = false
        layoutDetails.visibility = View.GONE
        tvStatusMessage.visibility = View.GONE
    }

    private fun finalizarCarregamento() {
        progressBar.visibility = View.GONE
        btnSearch.isEnabled = true
    }

    private fun exibirDetalhes(character: CharactersData) {
        val name = formatFieldValue(character.name)
        val species = formatFieldValue(character.species)
        val house = formatFieldValue(character.house)

        tvCharacterName.text = "Nome: $name"
        tvCharacterSpecies.text = "Espécie: $species"
        tvCharacterHouse.text = "Casa: $house"

        layoutDetails.visibility = View.VISIBLE
        tvStatusMessage.visibility = View.GONE
    }

    private fun exibirMensagem(mensagem: String) {
        layoutDetails.visibility = View.GONE
        tvStatusMessage.text = mensagem
        tvStatusMessage.visibility = View.VISIBLE
    }

    private fun formatFieldValue(value: String?): String {
        val trimmed = value?.trim()
        if (trimmed.isNullOrEmpty() || trimmed.equals("null", ignoreCase = true)) {
            return "Não informado"
        }
        return trimmed
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { focus ->
            imm?.hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }
}
