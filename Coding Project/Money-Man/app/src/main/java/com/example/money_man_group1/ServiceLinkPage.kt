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
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast

class ServiceLinkPage : AppCompatActivity() {
    private var spinnerCount = 0 // Counter for spinners

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

        // Set onClickListener for the Add Slot button
        addSlotButton.setOnClickListener {
            if (spinnerCount < 4) { // Check if the limit is less than 4
                // Create a new Spinner
                val newSpinner = Spinner(this).apply {
                    instructionsTextView.visibility = View.GONE // or View.INVISIBLE
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        // Set margin for the spinner
                        setMargins(0, 16, 0, 0) // Top margin of 16dp
                    }

                    // Create an ArrayAdapter for the Spinner
                    val options = arrayOf("Select An Account" ,"Bank Account", "Cashapp", "Paypal", "Venmo") // Example options
                    val adapter = ArrayAdapter(this@ServiceLinkPage, android.R.layout.simple_spinner_item, options)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    this.adapter = adapter
                }

                // Change the button text to say "Add Another Account"
                addSlotButton.text = "Add Another Account"

                // Add the new Spinner to the container
                val boxContainer = findViewById<LinearLayout>(R.id.box_container)
                boxContainer.addView(newSpinner)

                spinnerCount++ // Increment spinner count
            } else {
                Toast.makeText(this, "You can only add up to 4 accounts.", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
        }

        doneButton.setOnClickListener {
            if (spinnerCount > 0) { // Check if at least one spinner is added
                val intent = Intent(this, BudgetPage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please add at least one account before proceeding.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}