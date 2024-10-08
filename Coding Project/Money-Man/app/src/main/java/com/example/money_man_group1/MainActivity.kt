package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    companion object { //created so that logged in person data can be accessed from any class
        var currentLogin: Person? = null
    }
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
            val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString() //gets email from email box when login is pressed
            val personData = PersonData() //creates a new person
            currentLogin = personData.readPersonByEmail(this, email) //reads person from file and saves it

            if (currentLogin == null) { //if a person with the email isn't found, make notification
                Toast.makeText(this, "Email not Found!", Toast.LENGTH_SHORT).show()
            } else { //start next activity if person is found
                val intent = Intent(this, BudgetPage::class.java)
                startActivity(intent)
            }
        }

        val createAccountButton = findViewById<Button>(R.id.createAccountScreenButton)
        createAccountButton.setOnClickListener { //goes to create account screen
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
        }

    }
}