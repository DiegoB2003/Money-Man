package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ServiceLinkPage : AppCompatActivity() {
    // Navigation vars
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

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
                // Create a new container (LinearLayout) for the Spinner and Delete Button
                val accountContainer = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 16, 0, 0) // Top margin of 16dp
                    }
                }

                // Create a new Spinner
                val newSpinner = Spinner(this).apply {
                    instructionsTextView.visibility = View.GONE // or View.INVISIBLE
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f // Weight for equal spacing between Spinner and Button
                    )

                    // Create an ArrayAdapter for the Spinner
                    val options = arrayOf("Select An Account", "Bank Account", "Cashapp", "Paypal", "Venmo")
                    val adapter = ArrayAdapter(this@ServiceLinkPage, android.R.layout.simple_spinner_item, options)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    this.adapter = adapter
                }

                // Create a new Delete Button
                val deleteButton = Button(this).apply {
                    text = "Delete"
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(16, 0, 0, 0) // Left margin for spacing between Spinner and Delete Button
                    }

                    // Set OnClickListener to remove this account (Spinner and Delete Button)
                    setOnClickListener {
                        // Remove the container (which includes the spinner and this delete button)
                        val boxContainer = findViewById<LinearLayout>(R.id.box_container)
                        boxContainer.removeView(accountContainer)
                        spinnerCount-- // Decrease spinner count
                    }
                }

                // Add Spinner and Delete Button to the account container
                accountContainer.addView(newSpinner)
                accountContainer.addView(deleteButton)

                // Add the account container to the main container (boxContainer)
                val boxContainer = findViewById<LinearLayout>(R.id.box_container)
                boxContainer.addView(accountContainer)

                // Change the button text to say "Add Another Account"
                addSlotButton.text = "Add Another Account"
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

        // Nav view configurations
        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Add toggle to open and close drawer
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation menu item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.budgeting_page_button -> {
                    // Handle the budgeting page click
                    Toast.makeText(this, "Budgeting Page Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, BudgetPage::class.java)
                    startActivity(intent)
                }
                R.id.user_info_button -> {
                    // Handle the user info page click
                    Toast.makeText(this, "User Info Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserInfoPage::class.java)
                    startActivity(intent)
                }
                R.id.service_link_button -> {
                    // Handle linked accounts page click
                    Toast.makeText(this, "Already on Linked Accounts", Toast.LENGTH_SHORT).show()
                }
                R.id.logout_button -> {
                    // Handle logout
                    Toast.makeText(this, "Log Out Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Enable toggle button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks to open/close the drawer
        return if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(navView)
            true
        } else super.onOptionsItemSelected(item)
    }

}