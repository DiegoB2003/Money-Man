package com.example.money_man_group1

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.TabStopSpan
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
                val activityData = SpannableStringBuilder()
                val activityLogEntries = mutableListOf<UserActivityLog>()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault())

                // Tab stop for alignment
                val tabWidth = 80 // Adjust for your UI width

                // Collect log entries
                for (childSnapshot in snapshot.children) {
                    val logEntry = childSnapshot.getValue(UserActivityLog::class.java)
                    logEntry?.let { activityLogEntries.add(it) }
                }

                // Sort entries and format them for display
                activityLogEntries.sortedByDescending { it.timestamp }.forEach { entry ->
                    val formattedDate = dateFormat.format(Date(entry.timestamp))

                    // Create a single line with category and amount aligned
                    val categoryAmountLine = SpannableString("${entry.category.capitalize()} \t- $${String.format("%.2f", entry.amount)}").apply {
                        setSpan(TabStopSpan.Standard(tabWidth), entry.category.length + 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        setSpan(RelativeSizeSpan(1.3f), 0, entry.category.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Larger size for category
                        setSpan(StyleSpan(Typeface.BOLD), 0, entry.category.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Bold text for category
                        setSpan(RelativeSizeSpan(1.3f), entry.category.length + 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Larger size for amount
                        setSpan(StyleSpan(Typeface.BOLD), entry.category.length + 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Bold text for amount
                        setSpan(ForegroundColorSpan(Color.RED), entry.category.length + 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Red color for amount
                    }

                    // Create normal text for Message and Timestamp
                    val messageLine = SpannableString("\n\"${entry.message}\"")
                    val timestampLine = SpannableString("\nAt: $formattedDate\n")

                    // Append to the activity data
                    activityData.append(categoryAmountLine)
                    activityData.append(messageLine)
                    activityData.append(timestampLine)
                    activityData.append("─".repeat(25) + "\n") // Solid line separator

                }

                // Update UI
                textLog.text = activityData
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ActivityLog", "Error fetching data", error.toException())
            }
        })
    }

}