package com.example.money_man_group1
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.money_man_group1.databinding.ActivityUserInfoPageBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserInfoPage : AppCompatActivity() {
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database
    // Navigation vars
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var binding: ActivityUserInfoPageBinding

    // UI elements
    private lateinit var firstNameText: EditText
    private lateinit var lastNameText: EditText
    private lateinit var yearlyIncomeText: EditText
    private lateinit var userNameText: EditText
    private lateinit var phoneNumberText: EditText
    private lateinit var passwordText: EditText
    private lateinit var dobText: EditText
    private lateinit var editButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_info_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityUserInfoPageBinding.inflate(layoutInflater) // Inflate the binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Initialize UI elements
        firstNameText = findViewById(R.id.firstNameText)
        lastNameText = findViewById(R.id.LastNameText)
        yearlyIncomeText = findViewById(R.id.yearlyIncomeText)
        userNameText = findViewById(R.id.userNameText)
        phoneNumberText = findViewById(R.id.phoneNumberText)
        passwordText = findViewById(R.id.passwordText)
        dobText = findViewById(R.id.DOBText)
        editButton = findViewById(R.id.editButton)


        // Retrieve user data from MainActivity
        val userData = MainActivity.userData



        if (userData != null) {
            binding.textView7.text = userData.firstName
            binding.textView12.text = userData.lastName
            binding.textView15.text = userData.phoneNumber
          //  binding.textView16.text = userData.password
            binding.textView17.text = userData.dateOfBirth
            binding.textView13.text = userData.yearlyIncome.toString()
            binding.textView14.text = userData.username

            // Set EditTexts with original data and hide them initially
            firstNameText.setText(userData.firstName)
            lastNameText.setText(userData.lastName)
            yearlyIncomeText.setText(userData.yearlyIncome.toString())
            userNameText.setText(userData.username)
            phoneNumberText.setText(userData.phoneNumber)
            passwordText.setText(userData.password)
            dobText.setText(userData.dateOfBirth)

            // Initially hide EditText fields
            setEditTextVisibility(false)
        }
        else {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
        }

        binding.editButton.setOnClickListener {
            if (binding.editButton.text == "Save"){
                updateUserData(
                    binding.userNameText.text.toString(),
                    binding.firstNameText.text.toString(),
                    binding.LastNameText.text.toString(),
                    binding.phoneNumberText.text.toString(),
                    binding.passwordText.text.toString(),
                    binding.DOBText.text.toString(),
                    binding.yearlyIncomeText.text.toString()
                ) //calls update user data function to put all new values in database
                val intent = Intent(this, BudgetPage::class.java) //when clicked user info page goes to budget page
                startActivity(intent)
            }
            // Toggle visibility on edit button click
            val isEditing = binding.editButton.text == "Edit"
            setEditTextVisibility(isEditing)
            binding.editButton.text = if (isEditing) "Save" else "Edit"
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
            }
            drawerLayout.closeDrawers()
            true
        }

        val openDrawerButton: Button = findViewById(R.id.open_drawer_button)
        openDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START) // Open the drawer
        }

        // Enable toggle button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
    }
    private fun setEditTextVisibility(isEditing: Boolean) {
        // Set visibility of TextViews and EditTexts
        binding.textView7.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView12.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView15.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView16.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView17.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView13.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE
        binding.textView14.visibility = if (isEditing) View.INVISIBLE else View.VISIBLE

        firstNameText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        lastNameText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        yearlyIncomeText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        userNameText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        phoneNumberText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        passwordText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
        dobText.visibility = if (isEditing) View.VISIBLE else View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks to open/close the drawer
        return if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(navView)
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun updateUserData(username: String, firstName: String, lastName: String, phoneNumber: String, password: String, dateOfBirth: String, yearlyIncome: String) {
        //Make sure no values are empty
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            phoneNumber.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty() || yearlyIncome.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            return
        }

        val incomeDouble = yearlyIncome.toDouble() //Converts income to Double

        val oldUsername = MainActivity.userData?.username //Get the old username from userData
        MainActivity.userData = UserData(firstName, lastName, username, password, phoneNumber, incomeDouble, dateOfBirth) //Update userData with new values in MainActivity

        firebaseReference = FirebaseDatabase.getInstance().getReference("Users") //Get reference to Firebase

        //If the old username is not the same as the new one, remove the old entry from database
        if (oldUsername != null && oldUsername != username) {
            firebaseReference.child(oldUsername).removeValue() //Delete the old user from table

            renameFirebaseKey("userSpendingInfo", oldUsername,username) //now we rename the key in our userSpendingInfo table
            renameFirebaseKey("userActivityLog", oldUsername,username) //now we rename the key in our userActivityLog table
            renameFirebaseKey("userNotification", oldUsername,username) //now we rename the key in our userNotification table
        }

        //Insert the updated user data into Firebase
        firebaseReference.child(username).setValue(MainActivity.userData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

