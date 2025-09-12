package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BudgetPage : AppCompatActivity() {
    // Navigation vars
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    // Pie chart vars
    private val pieChartData: ArrayList<DataEntry> = ArrayList() //ArrayList to hold data for pie chart
    private lateinit var pieChart: Pie //Declare pie chart var
    private lateinit var anyChartView: AnyChartView //Declare chart view var

    //Database and user information
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database
    val userName: String = MainActivity.userData?.username ?: "Unknown User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        anyChartView = findViewById(R.id.any_chart_view) //gets pie chart object from xml page using its id
        pieChart = AnyChart.pie() //creates pie chart from library
        anyChartView.setChart(pieChart)

        if (MainActivity.isDarkModeEnabled) { //make pie chart background dark if in darkmode
            pieChart.background().fill("#000000")
        }

        firebaseReference = FirebaseDatabase.getInstance().getReference("userSpendingInfo") //gets instance of database

        getPieChartData() //gets data from database and adds it to the pie chart

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

        //setup the activity log button
        val activityLogButton: Button = findViewById(R.id.activiyLogButton)
        activityLogButton.setOnClickListener {
            val intent = Intent(this, ActivityLog::class.java)
            startActivity(intent)
        }

        val addCategory: Button = findViewById(R.id.addCatrgoryButton)
        addCategory.setOnClickListener {
            val intent = Intent(this, AddingCategoryPage::class.java)
            startActivity(intent)
        }

        val addSpending = findViewById<Button>(R.id.addSpendingButton) //setup the add spending button
        addSpending.setOnClickListener {
            val intent = Intent(this, SpendingPage::class.java)
            startActivity(intent)
        }

        val reloadPage = findViewById<Button>(R.id.reloaddataButton) //setup the reload page button
        reloadPage.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }

        // Handle navigation menu item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.budgeting_page_button -> {
                    // Handle the budgeting page click
                    Toast.makeText(this, "Already on Budgeting Page", Toast.LENGTH_SHORT).show()
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

    //Gets values from database and adds them to the pie chart
    private fun getPieChartData() {
        //Adds data to pie chart from firebase
        firebaseReference.child(userName).get().addOnSuccessListener { dataSnapshot ->
            val userSpendingInfo = dataSnapshot.getValue(UserSpendingInfo::class.java)

            if (userSpendingInfo != null) { //gets each category from the userSpendingInfo object
                val categories = listOf(
                    userSpendingInfo.categoryOne,
                    userSpendingInfo.categoryTwo,
                    userSpendingInfo.categoryThree,
                    userSpendingInfo.categoryFour,
                    userSpendingInfo.categoryFive,
                    userSpendingInfo.categorySix
                )

                val addSpendingTextView = findViewById<TextView>(R.id.addSpendingText)
                var valuesInChart = false

                //Checks if each category is active and if it is add to pie chart
                for (category in categories) {
                    if (category.isCategoryActive == "Yes" && category.currentMoneySpent != 0.0) {
                        addToChart(category.categoryName, category.currentMoneySpent)
                        valuesInChart = true
                    }
                }
                if (!valuesInChart) {
                    addSpendingTextView.visibility = TextView.VISIBLE
                } else {
                    addSpendingTextView.visibility = TextView.INVISIBLE
                }
            }
        }
        pieChart.title("Total Spending Breakdown")
        pieChart.title().fontSize(25)
        pieChart.labels().position("inside").format("\${%value}").fontSize(15)
        pieChart.legend().title().enabled(true)
        pieChart.legend().title()
            .text("Categories")
            .padding(0.0, 0.0, 15.0, 0.0)
            .fontSize(25)
        pieChart.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
            .align(Align.CENTER)
        pieChart.innerRadius("25%")
        pieChart.radius("150%")
        anyChartView.refreshDrawableState()
    }

    //function adds name and cost of what user spend into the
    //pieChartData arraylist and then sets the new data to
    //the pie chart so it is displayed
    private fun addToChart (name: String, cost: Double) {
        pieChartData.add(ValueDataEntry(name, cost)) //adds values to arraylist
        pieChart.data(pieChartData)//sets the array of data to the pie chart to display
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks to open/close the drawer
        return if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(navView)
            true
        } else super.onOptionsItemSelected(item)
    }
}