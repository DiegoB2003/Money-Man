package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class NotificationsPage : AppCompatActivity() {

    // Notifications Recycler View vars

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationsAdapter

    // Navigation vars
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notifications_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.notificationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data (replace with actual notifications later)
        val notifications = listOf(
            "Alert: Your budget is over the limit!",
            "Notification: New category added",
            "Alert: You're so damn broke!"
        )

        // Set up recycler view adapter
        adapter = NotificationsAdapter(notifications)
        recyclerView.adapter = adapter

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
                    Toast.makeText(this, "Add Categories Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddingCategoryPage::class.java)
                    startActivity(intent)
                }
                R.id.notifications_page_button -> {
                    // Handle linked accounts page click
                    Toast.makeText(this, "Already on Notifications Page", Toast.LENGTH_SHORT).show()
                }
                R.id.logout_button -> {
                    // Handle logout
                    Toast.makeText(this, "Log Out Clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.close_button -> {
                    drawerLayout.closeDrawers() // Close the drawer
                }

            }
            drawerLayout.closeDrawers()
            true
        }
        // Set up the button to open the drawer #################NEW
        val openDrawerButton: Button = findViewById(R.id.open_drawer_button)
        openDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START) // Open the drawer
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
