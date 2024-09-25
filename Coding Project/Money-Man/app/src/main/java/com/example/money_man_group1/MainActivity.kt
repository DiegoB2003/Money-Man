package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginButton = findViewById<Button>(R.id.loginScreenButton)
        loginButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }

        val createAccountButton = findViewById<Button>(R.id.createAccountScreenButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
        }

    }
}