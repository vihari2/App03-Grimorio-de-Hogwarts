package com.example.app3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class CreatureActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnBack: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var llResult: LinearLayout
    private lateinit var tvError: TextView
    private lateinit var tvName: TextView
    private lateinit var ivCreature: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creature)

        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        btnBack = findViewById(R.id.btnBack)
        progressBar = findViewById(R.id.progressBar)
        llResult = findViewById(R.id.llResult)
        tvError = findViewById(R.id.tvError)
        tvName = findViewById(R.id.tvName)
        ivCreature = findViewById(R.id.ivCreature)

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
            buscarCriatura(query)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun buscarCriatura(query: String) {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.hogwartsApi.getCreatures(query)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val creature: CreaturesData? = response.body()?.getBestMatchCreature(query)
                        if (creature != null) {
                            showCreature(creature)
                        } else {
                            showCreatureNotFound()
                        }
                    } else if (response.code() == 404) {
                        showCreatureNotFound()
                    } else {
                        hideLoading()
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
        btnBack.isEnabled = false
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        btnSearch.isEnabled = true
        btnBack.isEnabled = true
    }

    private fun showError(mensagem: String) {
        llResult.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        tvError.text = mensagem
    }

    private fun showCreatureNotFound() {
        hideLoading()
        tvError.visibility = View.GONE
        llResult.visibility = View.VISIBLE
        tvName.text = "Nenhuma criatura encontrada"
        ivCreature.setImageResource(R.drawable.placeholder_criatura)
    }

    private fun showCreature(creaturesData: CreaturesData) {
        tvError.visibility = View.GONE
        llResult.visibility = View.VISIBLE
        tvName.text = "Nome: ${creaturesData.name ?: "Não informado"}"
        
        val imageUrl = creaturesData.image
        if (imageUrl.isNullOrEmpty()) {
            hideLoading()
            ivCreature.setImageResource(R.drawable.placeholder_criatura) 
            tvError.visibility = View.VISIBLE
            tvError.text = "Imagem indisponível"
        } else {
            // Keep progress bar active while image downloads
            progressBar.visibility = View.VISIBLE
            btnSearch.isEnabled = false
            btnBack.isEnabled = false

            ivCreature.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.placeholder_criatura)
                error(R.drawable.placeholder_criatura)
                listener(
                    onSuccess = { _, _ -> 
                        hideLoading()
                        tvError.visibility = View.GONE
                    },
                    onError = { _, _ -> 
                        hideLoading()
                        tvError.visibility = View.VISIBLE
                        tvError.text = "Imagem indisponível"
                    }
                )
            }
        }
    }
}
