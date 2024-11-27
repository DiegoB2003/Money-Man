package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                val activityLogEntries = mutableListOf<UserActivityLog>()

                // Setup date format
                val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault())

                // Iterate through the user activity log entries
                for (childSnapshot in snapshot.children) {
                    val logEntry = childSnapshot.getValue(UserActivityLog::class.java)
                    if (logEntry != null) {
                        activityLogEntries.add(logEntry)
                    }
                }

                // Sort entries by timestamp in descending order
                val sortedEntries = activityLogEntries.sortedByDescending { it.timestamp }

                // Format and append the sorted entries to the StringBuilder
                for (entry in sortedEntries) {
                    val date = Date(entry.timestamp)
                    val formattedDate = dateFormat.format(date)
                    activityData.append("Amount: ${entry.amount}\n")
                    activityData.append("Category: ${entry.category}\n")
                    activityData.append("Message: ${entry.message}\n")
                    activityData.append("Timestamp: $formattedDate\n\n")
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