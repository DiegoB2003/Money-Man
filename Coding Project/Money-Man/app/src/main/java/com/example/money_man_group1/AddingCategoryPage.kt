
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
import com.google.firebase.database.FirebaseDatabase
import android.util.Log



class AddingCategoryPage : AppCompatActivity() {
    // Navigation vars
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    //map will help us map the category name to its actual numerical value
    val categoryToNumberMap = mapOf(
        1 to "categoryOne",
        2 to "categoryTwo",
        3 to "categoryThree",
        4 to "categoryFour",
        5 to "categoryFive",
        6 to "categorySix"
    )
    val userName: String = MainActivity.userData?.username
        ?: "Unknown User"    //retrieve the username of the user to access its data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adding_category_page)

        // Initialize counter to 6 to display all categories initially
        var counter = 6

        // Access info from the database
        firebaseReference = FirebaseDatabase.getInstance().getReference("userSpendingInfo")
        firebaseReference.child(userName).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val userSpendingInfo = dataSnapshot.getValue(UserSpendingInfo::class.java)
                userSpendingInfo?.let {
                    counter = it.numberOfSpendingCategories
                    Log.d("UserSpendingInfo", "Number of Spending Categories: $counter")
                }
            } else {
                Log.d("UserSpendingInfo", "No data found for user: $userName")
            }
        }.addOnFailureListener { exception ->
            Log.e("UserSpendingInfo", "Error getting data", exception)
        }

        // Loop to create six category sections
        val boxContainer = findViewById<LinearLayout>(R.id.box_container)
        val instructionsTextView = findViewById<TextView>(R.id.instructions)
        instructionsTextView.visibility = View.GONE // Hide instructions

// Loop to create six category sections
        for (i in 1..6) {


            val verticalLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 32, 0, 0)
                }
            }

            // EditText fields for the category name, description, and spending limit
            val mainEditText = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                hint = "Enter Category Name"
            }

            val categoryDesc = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                hint = "Brief Description"
            }

            val maxSpendingLimit = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                hint = "Max Spending Limit"
            }

            // Check if the category is active and retrieve data
            firebaseReference.child(userName).child(categoryToNumberMap[i].toString()).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val categoryActive =
                            dataSnapshot.child("categoryActive").getValue(String::class.java)
                                ?: "No"
                        if (categoryActive == "Yes") {
                            // Set actual values if category is active
                            val categoryName =
                                dataSnapshot.child("categoryName").getValue(String::class.java)
                                    ?: ""
                            val categoryDescription = dataSnapshot.child("categoryDescription")
                                .getValue(String::class.java) ?: ""
                            val spendingLimit =
                                dataSnapshot.child("maxSpendingLimit").getValue(Int::class.java)
                                    ?: 0

                            // Set the values in the EditText fields
                            mainEditText.setText(categoryName)
                            categoryDesc.setText(categoryDescription)
                            maxSpendingLimit.setText(spendingLimit.toString())
                        }
                    }
                }.addOnFailureListener { exception ->
                Log.e("UserSpendingInfo", "Error getting data", exception)
            }

            // Horizontal layout for the category description and spending limit
            val horizontalLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Button layout
            val buttonLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 16, 0, 0)
                }
            }

            // Buttons to delete and set the category
            val deleteButton = Button(this).apply {
                text = "Remove"
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setOnClickListener {

                    //if the category box is empty, then dont do anything
                    if (mainEditText.text.isEmpty()) {

                        Toast.makeText(
                            this@AddingCategoryPage,
                            "This is blank",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    else {
                        // Clear the values and reset database entries
                        mainEditText.text.clear()
                        categoryDesc.text.clear()
                        maxSpendingLimit.text.clear()

                        // Reset the values in the database
                        val categoryNumber = categoryToNumberMap[i]
                        firebaseReference.child(userName).child(categoryNumber.toString())
                            .child("categoryActive").setValue("No")
                        firebaseReference.child(userName).child(categoryNumber.toString())
                            .child("categoryName").setValue("")
                        firebaseReference.child(userName).child(categoryNumber.toString())
                            .child("categoryDescription").setValue("")
                        firebaseReference.child(userName).child(categoryNumber.toString())
                            .child("maxSpendingLimit").setValue(0.0)
                        firebaseReference.child(userName).child(categoryNumber.toString())
                            .child("currentMoneySpent").setValue(0.0)


                        //set a toast message to let the user know that the category has been removed
                        Toast.makeText(
                            this@AddingCategoryPage,
                            "Category has been deleted",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Decrement the counter and update the number of categories
                        counter--
                        firebaseReference.child(userName).child("numberOfSpendingCategories")
                            .setValue(counter)
                    }
                }
            }

            val setButton = Button(this).apply {
                text = "Set"
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

                setOnClickListener {
                    val categoryName = mainEditText.text.toString()
                    val categoryDescription = categoryDesc.text.toString()
                    val spendingLimit = maxSpendingLimit.text.toString().toDoubleOrNull() ?: 0.0

                    val categoryNumber = categoryToNumberMap[i]

                    // Retrieve the current status of the category
                    firebaseReference.child(userName).child(categoryNumber.toString()).get().addOnSuccessListener { dataSnapshot ->
                        val categoryCurrentStatus = dataSnapshot.child("categoryActive").getValue(String::class.java)
                        if (categoryCurrentStatus == "No") {
                            counter++
                        }

                        // Update the database with the new values
                        firebaseReference.child(userName).child(categoryNumber.toString()).child("categoryActive").setValue("Yes")
                        firebaseReference.child(userName).child(categoryNumber.toString()).child("categoryName").setValue(categoryName)
                        firebaseReference.child(userName).child(categoryNumber.toString()).child("categoryDescription").setValue(categoryDescription)
                        firebaseReference.child(userName).child(categoryNumber.toString()).child("maxSpendingLimit").setValue(spendingLimit)

                        //now give the user a toast message to let them know that the category has been set
                        Toast.makeText(this@AddingCategoryPage, "Category has been set", Toast.LENGTH_SHORT).show()

                        firebaseReference.child(userName).child("numberOfSpendingCategories").setValue(counter)
                    }.addOnFailureListener { exception ->
                        Log.e("UserSpendingInfo", "Error getting data", exception)
                    }
                }
            }
                // Add the buttons to the button layout
                buttonLayout.addView(deleteButton)
                buttonLayout.addView(setButton)
                horizontalLayout.addView(categoryDesc)
                horizontalLayout.addView(maxSpendingLimit)
                verticalLayout.addView(mainEditText)
                verticalLayout.addView(horizontalLayout)
                verticalLayout.addView(buttonLayout)
                boxContainer.addView(verticalLayout)
            }

            //make the done button to go to the budget page
            val doneButton = findViewById<Button>(R.id.doneButton)
            doneButton.setOnClickListener {
                val intent = Intent(this, BudgetPage::class.java)
                startActivity(intent)
            }
        }
    }

