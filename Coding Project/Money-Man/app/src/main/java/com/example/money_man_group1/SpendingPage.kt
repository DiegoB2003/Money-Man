package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SpendingPage : AppCompatActivity() {
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

        val categoryNameText = findViewById<EditText>(R.id.categoryEditText) //Get the EditText category
        val amountSpentText = findViewById<EditText>(R.id.amountSpentEditText) //Get the EditText amount spent

        val saveButton = findViewById<Button>(R.id.saveButton)
        firebaseReference = FirebaseDatabase.getInstance().getReference("userSpendingInfo") //gets instance of database
        saveButton.setOnClickListener {
            val categoryNameT = categoryNameText.text.toString().trim() //gets users wanted category
            val amountSpentT = amountSpentText.text.toString().toDouble() //gets users amount spent
            var categoryFound = false //flag to check if category is found

            for (i in 1..6) { //loops through categories to find correct one
                val categoryNumber = categoryToNumberMap[i] //gets category number as a name
                val categoryRef = firebaseReference.child(userName).child(categoryNumber.toString())

                categoryRef.child("categoryName").get().addOnSuccessListener { dataSnapshot ->
                    val categoryNameFromDB = dataSnapshot.value as? String //gets category name from database
                    if (categoryNameFromDB == categoryNameT) { //category name matches databases
                        categoryFound = true

                        //Retrieve currentMoneySpent
                        categoryRef.child("currentMoneySpent").get().addOnSuccessListener { dataS ->
                            val currentMoneySpent = (dataS.value as? Number)?.toDouble() ?: 0.0
                            val updatedAmount = amountSpentT + currentMoneySpent

                            //Updates currentMoneySpent
                            categoryRef.child("currentMoneySpent").setValue(updatedAmount).addOnSuccessListener {
                                Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                                categoryNameText.text.clear() //Clears boxes for another user entry
                                amountSpentText.text.clear()
                            }
                        }
                        return@addOnSuccessListener
                    }
                }.addOnCompleteListener { //Checks if category is found
                    if (!categoryFound && i == 6) {
                        Toast.makeText(this, "Category not found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val backButton = findViewById<Button>(R.id.backbutton)
        backButton.setOnClickListener {
            val intent = Intent(this, BudgetPage::class.java)
            startActivity(intent)
        }
    }
}