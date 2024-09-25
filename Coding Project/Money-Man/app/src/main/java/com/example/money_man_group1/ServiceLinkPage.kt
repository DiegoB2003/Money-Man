package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View

class ServiceLinkPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_link_page)

        // Apply window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the button inside the onCreate() method
        val addSlotButton = findViewById<Button>(R.id.add_slot_button)
        val backButton = findViewById<Button>(R.id.backbutton)
        val doneButton = findViewById<Button>(R.id.donebutton)
        val instructionsTextView = findViewById<TextView>(R.id.instructions)

        // Set onClickListener inside onCreate() method
        addSlotButton.setOnClickListener {
            val newBox = TextView(this).apply {
                instructionsTextView.visibility = View.GONE // or View.INVISIBLE
                text = "New Account Slot"
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            // Add the new TextView to the container
            val boxContainer = findViewById<LinearLayout>(R.id.box_container)
            boxContainer.addView(newBox)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
        }

        doneButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }
    }
}