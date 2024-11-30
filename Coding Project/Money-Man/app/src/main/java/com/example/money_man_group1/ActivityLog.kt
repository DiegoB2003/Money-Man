package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityLog : AppCompatActivity() {

    private lateinit var firebaseReference: DatabaseReference
    private lateinit var textLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        // Initialize Firebase and UI elements
        firebaseReference = FirebaseDatabase.getInstance().reference
        textLog = findViewById(R.id.text_log)

        // Get username
        val userName = MainActivity.userData?.username ?: "Unknown User"
        Log.d("ActivityLog", "Fetched userName: $userName")

        // Fetch and display the activity log
        fetchActivityLog(userName)

        // Set up the back button
        findViewById<Button>(R.id.backbutton).setOnClickListener {
            startActivity(Intent(this, BudgetPage::class.java))
        }
    }

    private fun fetchActivityLog(userName: String) {
        val userLogRef = firebaseReference.child("userActivityLog").child(userName)

        userLogRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activityData = StringBuilder()
                val activityLogEntries = mutableListOf<UserActivityLog>()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault())

                // Collect log entries
                for (childSnapshot in snapshot.children) {
                    val logEntry = childSnapshot.getValue(UserActivityLog::class.java)
                    logEntry?.let { activityLogEntries.add(it) }
                }

                // Sort entries and format them for display
                activityLogEntries.sortedByDescending { it.timestamp }.forEach { entry ->
                    val formattedDate = dateFormat.format(Date(entry.timestamp))
                    activityData.append(
                        "Amount: ${entry.amount}\n" +
                                "Category: ${entry.category}\n" +
                                "Message: ${entry.message}\n" +
                                "Timestamp: $formattedDate\n"
                    )
                    activityData.append("—".repeat(15) + "\n") // Add a line divider between messages
                }

                // Update UI
                textLog.text = activityData.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityLog", "Error fetching data", error.toException())
            }
        })
    }
}
