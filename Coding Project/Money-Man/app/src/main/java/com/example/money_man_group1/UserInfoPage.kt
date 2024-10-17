package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class UserInfoPage : AppCompatActivity() {
    // Navigation vars
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_info_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userInfoButton = findViewById<Button>(R.id.userInfoButton) //creates save button
        userInfoButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java) //when clicked user info page goes to budget page
            startActivity(intent)
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
                    Toast.makeText(this, "Already on User Info", Toast.LENGTH_SHORT).show()
                }
                R.id.service_link_button -> {
                    // Handle linked accounts page click
                    Toast.makeText(this, "Linked Accounts Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddingCategoryPage::class.java)
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