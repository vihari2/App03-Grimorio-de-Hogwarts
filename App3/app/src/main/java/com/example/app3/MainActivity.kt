package com.example.app3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnPersonagem: Button
    private lateinit var btnCriatura: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnPersonagem = findViewById(R.id.button)
        btnCriatura = findViewById(R.id.button2)

        btnPersonagem.setOnClickListener {
            startActivity(Intent(this, CharacterActivity::class.java))
        }

        btnCriatura.setOnClickListener {
            startActivity(Intent(this, CreatureActivity::class.java))
        }
    }
}
