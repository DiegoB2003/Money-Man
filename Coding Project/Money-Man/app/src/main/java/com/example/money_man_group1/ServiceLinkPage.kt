
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

        // Set onClickListener for the Add Slot
        addSlotButton.setOnClickListener {
            // Check if the spinner count is greater than or equal to 4, and display a toast message if so
            if (spinnerCount >= 4) { // Check if the spinner count is greater than or equal to 4
                Toast.makeText(this, "You can only add up to 4 accounts.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                // Create a horizontal layout to hold the spinner and delete button
                val horizontalLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 32, 0, 0) // Increase top margin for more space between spinners (32dp)
                    }
                }

                // Create the new Spinner
                val newSpinner = Spinner(this).apply {
                    instructionsTextView.visibility =
                        View.GONE // Hide instructions if spinner is added
                    layoutParams = LinearLayout.LayoutParams(
                        0, // Set weight of the spinner to take up available space
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f

                    )

                    spinnerCount++ // Increment spinner count

                    // Create an array adapter for the spinner and set the dropdown items
                    val options =
                        arrayOf("Select An Account", "Bank Account", "CashApp", "Paypal", "Venmo")
                    val adapter = ArrayAdapter(
                        this@ServiceLinkPage,
                        android.R.layout.simple_spinner_item,
                        options
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    this.adapter = adapter
                }

                // Create the delete button
                val deleteButton = Button(this).apply {
                    text = "Delete"
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setOnClickListener {
                        // Remove the horizontal layout containing the spinner and delete button
                        (horizontalLayout.parent as? LinearLayout)?.removeView(horizontalLayout)
                        --spinnerCount // Decrement spinner count

                        if (spinnerCount == 0) { // Check if spinner count is 0
                            // Show instructions if spinner is deleted
                            instructionsTextView.visibility = View.VISIBLE
                        }
                    }


                }

                // Add the spinner and delete button to the horizontal layout
                horizontalLayout.addView(newSpinner)
                horizontalLayout.addView(deleteButton)

                // Add the horizontal layout to the container
                val boxContainer = findViewById<LinearLayout>(R.id.box_container)
                boxContainer.addView(horizontalLayout)


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
                Toast.makeText(
                    this,
                    "Please add at least one account before proceeding.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Nav view configurations
        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Add toggle to open and close drawer
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
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
                    Toast.makeText(this, "Already on Linked Accounts", Toast.LENGTH_SHORT)
                        .show()
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

