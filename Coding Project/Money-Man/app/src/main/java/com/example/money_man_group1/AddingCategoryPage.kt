
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
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference

class AddingCategoryPage : AppCompatActivity() {
    // Navigation vars
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private var categoryCount = 0 // Counter for spinners



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adding_category_page)


        // Apply window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the button inside the onCreate() method
        val addSlotButton = findViewById<Button>(R.id.add_Category_button)
        val backButton = findViewById<Button>(R.id.backbutton)
        val doneButton = findViewById<Button>(R.id.donebutton)
        val userData = MainActivity.userData    //used to access the user info
        val instructionsTextView = findViewById<TextView>(R.id.instructions)

        //HOW TO ACCESS USERNAME
        //  userData.username


        // Set onClickListener for the Add Slot
        addSlotButton.setOnClickListener {


            if (categoryCount >= 6) {
                Toast.makeText(this, "You can only add up to 6 Categories.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                // Hide the instructions text view
                instructionsTextView.visibility = View.GONE

                // Create a vertical layout to hold the EditText boxes and the delete button
                val verticalLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 32, 0, 0) // Increase top margin for more space
                    }
                }

                // Create the main EditText
                val mainEditText = EditText(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    hint = "Enter Category Name"
                    categoryCount++
                }

                // Create a horizontal layout to contain the two smaller EditTexts
                val horizontalLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                // Create the first smaller EditText
                val smallerEditText1 = EditText(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0, // Set weight to distribute space evenly
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f // Use 1 weight to make it smaller compared to the main EditText
                    )
                    hint = "Brief Description"
                }

                // Create the second smaller EditText
                val smallerEditText2 = EditText(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    hint = "Max Spending Limit"
                }

                // Create a horizontal layout to hold the buttons
                val buttonLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 16, 0, 0) // Adjust margin as needed
                    }
                }

                // Create the delete button
                val deleteButton = Button(this).apply {
                    text = "Delete"
                    layoutParams = LinearLayout.LayoutParams(
                        0, // Weight set to 0 to take up a proportional space
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f // Set weight to 1 for equal width among buttons
                    )
                    setOnClickListener {
                        (verticalLayout.parent as? LinearLayout)?.removeView(verticalLayout)
                        --categoryCount

                        if (categoryCount == 0) {
                            instructionsTextView.visibility = View.VISIBLE
                        }
                    }
                }

                // Create the set button
                val setButton = Button(this).apply {
                    text = "Set"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                }

                // Add the buttons to the button layout horizontally
                buttonLayout.addView(deleteButton)
                buttonLayout.addView(setButton)

                // Add the smaller EditTexts to the horizontal layout
                horizontalLayout.addView(smallerEditText1)
                horizontalLayout.addView(smallerEditText2)

                // Add the main EditText, the horizontal layout with smaller EditTexts, and the delete button to the vertical layout
                verticalLayout.addView(mainEditText)
                verticalLayout.addView(horizontalLayout)

                // Add the button layout to the vertical layout
                verticalLayout.addView(buttonLayout)

                // Add the vertical layout to the container
                val boxContainer = findViewById<LinearLayout>(R.id.box_container)
                boxContainer.addView(verticalLayout)
            }
        }

        // Back button to go back to create account page
        backButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }

        doneButton.setOnClickListener {
            if (categoryCount > 0) { // Check if at least one spinner is added
                val intent = Intent(this, BudgetPage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Please add at least one category before proceeding.",
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
                    Toast.makeText(this, "Already on Add Categories", Toast.LENGTH_SHORT)
                        .show()
                }

                R.id.notifications_page_button -> {
                    // Handle linked accounts page click
                    Toast.makeText(this, "Notifications Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, NotificationsPage::class.java)
                    startActivity(intent)
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

