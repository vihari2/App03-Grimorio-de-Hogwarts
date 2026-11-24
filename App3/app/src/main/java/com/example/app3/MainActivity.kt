package com.example.app3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

class MainActivity : AppCompatActivity() {

    private lateinit var btnPersonagem: Button
    private lateinit var btnCriatura: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        btnPersonagem = findViewById(R.id.button)
        btnCriatura = findViewById(R.id.button2)
        progressBar = findViewById(R.id.progressBar)
        tvStatus = findViewById(R.id.tvStatus)

        btnPersonagem.setOnClickListener {
            val intent = Intent(this, CharacterSearchActivity::class.java)
            startActivity(intent)
        }

        btnCriatura.setOnClickListener {
            carregarCriatura()
        }
    }

    private fun carregarCriatura() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.hogwartsApi.getCreatures()
                withContext(Dispatchers.Main) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val creature: CreaturesData? = response.body()?.getFirstCreature()
                        if (creature != null) {
                            showStatus("Criatura: ${creature.name ?: "Sem nome"}")
                        } else {
                            showStatus("Nenhuma criatura encontrada")
                        }
                    } else {
                        showStatus("Erro no servidor (código ${response.code()}). Tente novamente.")
                    }
                }
            } catch (_: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showStatus("Sem conexão com a internet. Tente novamente.")
                }
            } catch (_: IOException) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showStatus("Sem conexão com a internet. Tente novamente.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                    showStatus("Erro inesperado: ${e.localizedMessage ?: "Tente novamente."}")
                }
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        tvStatus.visibility = View.GONE
        btnPersonagem.isEnabled = false
        btnCriatura.isEnabled = false
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        btnPersonagem.isEnabled = true
        btnCriatura.isEnabled = true
    }

    private fun showStatus(mensagem: String) {
        tvStatus.visibility = View.VISIBLE
        tvStatus.text = mensagem
    }
}
