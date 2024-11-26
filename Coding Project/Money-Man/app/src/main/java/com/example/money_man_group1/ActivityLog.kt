package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class ActivityLog : AppCompatActivity() {

    private lateinit var firebaseReference: DatabaseReference
    private lateinit var textLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        // Setup Firebase and initialize variables
        firebaseReference = FirebaseDatabase.getInstance().reference
        textLog = findViewById(R.id.text_log)

        // Get userName
        val userName: String = MainActivity.userData?.username ?: "Unknown User"
        Log.d("ActivityLog", "Fetched userName: $userName")

        // Fetch data from Firebase
        fetchActivityLog(userName)

        // Setup back button
        val backButton: Button = findViewById(R.id.backbutton)
        backButton.setOnClickListener {
            startActivity(Intent(this, BudgetPage::class.java))
        }
    }

    // Function to fetch activity log from Firebase
    private fun fetchActivityLog(userName: String) {
        val userLogRef = firebaseReference.child("userActivityLog").child(userName)

        userLogRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear previous data
                val activityData = StringBuilder()

                // Iterate through the user activity log entries
                for (childSnapshot in snapshot.children) {
                    val amount = childSnapshot.child("amount").getValue(Int::class.java) ?: 0
                    val category = childSnapshot.child("category").getValue(String::class.java) ?: "N/A"
                    val message = childSnapshot.child("message").getValue(String::class.java) ?: "N/A"
                    val timestamp = childSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L

                    // Append the fetched data to the StringBuilder
                    activityData.append("Amount: $amount\n")
                    activityData.append("Category: $category\n")
                    activityData.append("Message: $message\n")
                    activityData.append("Timestamp: $timestamp\n\n")
                }

                // Display the fetched data in the text log
                textLog.text = activityData.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityLog", "Error fetching data", error.toException())
            }
        })
    }
}
