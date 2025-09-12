package com.example.money_man_group1

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import com.anychart.data.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.View

import com.example.money_man_group1.NotificationUtils

class SpendingPage : AppCompatActivity() {
    // Define a request code and pending message
    private var pendingNotificationMessage: String? = null
    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var firebaseReference: DatabaseReference //reference to firebase database
    val userName: String = MainActivity.userData?.username ?: "Unknown User"

    val categoryToNumberMap = mapOf(
        1 to "categoryOne",
        2 to "categoryTwo",
        3 to "categoryThree",
        4 to "categoryFour",
        5 to "categoryFive",
        6 to "categorySix"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spending_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //setup variables
        val descriptionText = findViewById<EditText>(R.id.descriptionEditText) //Get the EditText category
        val amountSpentText = findViewById<EditText>(R.id.amountSpentEditText) //Get the EditText amount spent
        val dropdownMenuWithCategory = findViewById<Spinner>(R.id.categortMenu) //Get the spinner
        val saveButton = findViewById<Button>(R.id.saveButton)
//        val testButton = findViewById<Button>(R.id.testButton)

        firebaseReference = FirebaseDatabase.getInstance().getReference("userSpendingInfo") //gets instance of database

        //get the userName from the MainActivity
        val userName = MainActivity.userData?.username ?: "Unknown User"

        // Create a list for the categories
        val categoryList = mutableListOf<String>()

        //add a "select an option" to the list
        categoryList.add("Select a category")
        val totalCategories = 6 // total number of categories

        // Loop through all categories and add the active ones to the list
        for (i in 1..totalCategories) {
            val categoryNumber = categoryToNumberMap[i]
            val categoryRef = firebaseReference.child(userName).child(categoryNumber.toString())

            // Correct the field name from "active" to "categoryActive" based on your database structure
            categoryRef.child("categoryActive").get().addOnSuccessListener { dataSnapshot ->
                val active = dataSnapshot.value as? String
                if (active == "Yes") {  // Check if the category is active

                    // Get the category name
                    categoryRef.child("categoryName").get().addOnSuccessListener { dataSnap ->
                        val categoryName = dataSnap.value as? String

                        // Add the category name to the list
                        if (categoryName != null) {
                            categoryList.add(categoryName)
                        }
                    }
                }
            }
        }

        //now we setup the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdownMenuWithCategory.adapter = adapter

        // Initialize the selectedCategory and selectedCategoryKey variables
        var selectedCategory: String? = null
        var selectedCategoryKey: String? = null


        // Set an OnItemSelectedListener on the spinner
        dropdownMenuWithCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // When an item is selected, set the selectedCategory and selectedCategoryKey variables
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    selectedCategory = categoryList[position]
                    selectedCategoryKey = categoryToNumberMap[position]
                } else {
                    selectedCategory = null
                    selectedCategoryKey = null
                    descriptionText.text.clear()
                    amountSpentText.text.clear()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // If nothing is selected, set the selectedCategory and selectedCategoryKey variables to null
                selectedCategory = null
                selectedCategoryKey = null
                descriptionText.text.clear()
                amountSpentText.text.clear()
            }
        }


        saveButton.setOnClickListener {
            saveButton.isClickable = false

            val amountSpentT = amountSpentText.text.toString().toDoubleOrNull() // gets user's amount spent
            val selectedCategoryT = selectedCategory?.trim() // retrieves the selected category name
            val selectedCategoryKeyT = selectedCategoryKey?.trim() // retrieves the selected category key

            if (amountSpentT == null || selectedCategoryT.isNullOrEmpty() || selectedCategoryKeyT.isNullOrEmpty() || selectedCategoryT == "Select a category") {
                Toast.makeText(this, "Please provide valid inputs!", Toast.LENGTH_SHORT).show()
                saveButton.isClickable = true
                return@setOnClickListener
            }

            // Reference to the selected category in Firebase
            val categoryRef = firebaseReference.child(userName).child(selectedCategoryKeyT)

            // Retrieve currentMoneySpent
            categoryRef.child("currentMoneySpent").get().addOnSuccessListener { dataSnapshot ->
                val currentMoneySpent = (dataSnapshot.value as? Number)?.toDouble() ?: 0.0
                val updatedAmount = amountSpentT + currentMoneySpent

                // Update currentMoneySpent
                categoryRef.child("currentMoneySpent").setValue(updatedAmount).addOnSuccessListener {
                    Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                    descriptionText.text.clear() // Clears input fields
                    amountSpentText.text.clear()

                    // Check and notify if over the maxSpendingLimit
                    categoryRef.child("maxSpendingLimit").get().addOnSuccessListener { maxLimitSnapshot ->
                        val maxSpendingLimit = (maxLimitSnapshot.value as? Number)?.toDouble() ?: 0.0
                        var message: String? = null

                        when {
                            updatedAmount > maxSpendingLimit -> {
                                val formattedAmount = String.format("%.2f", updatedAmount - maxSpendingLimit)
                                message = "You are $$formattedAmount over the spending limit of $selectedCategoryT!"
                            }
                            updatedAmount == maxSpendingLimit -> {
                                message = "You have reached the spending limit of $selectedCategoryT!"
                            }
                        }

                        message?.let {
                            NotificationUtils.sendNotification(this, it)
                        }
                    }
                }

                //now we add the information of the money spent and the description to the database
                val spentInfo = UserActivityLog(
                    message = descriptionText.text.toString(),
                    amount = amountSpentT,
                    category = selectedCategoryT
                )

                val userActivityLogRef = FirebaseDatabase.getInstance().getReference("userActivityLog").child(userName)
                userActivityLogRef.push().setValue(spentInfo)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to save data. Please try again!", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener {
                saveButton.isClickable = true
            }
        }


        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, send the notification
                pendingNotificationMessage?.let {
                    NotificationUtils.sendNotification(this, it)
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}