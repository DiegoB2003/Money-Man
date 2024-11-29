package com.example.money_man_group1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Define a request code
private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

object NotificationUtils {

    //Database and user information
    val userName: String = MainActivity.userData?.username ?: "Unknown User"

    fun sendNotification(context: Context, message: String) {
        val channelId = "alerts_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Create notification channel (necessary for API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alerts"
            val descriptionText = "Channel for user alerts and notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Intent for notification tap
        val intent = Intent(context, ActivityLog::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create and send the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Budget Alert!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Check if the permission is already granted
                if (ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Request the permission
                    ActivityCompat.requestPermissions(
                        (context as Activity),
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    // Permission already granted, send the notification
                    notify(notificationId, builder.build())
                }
            } else {
                // Permission not required below Android 13, send the notification
                notify(notificationId, builder.build())
            }
        }

        // Save the notification to Firebase
        val notification = Notification(
            message = message
        )
        storeNotificationInFirebase(userName, notification)
    }

    private fun storeNotificationInFirebase(username: String, notification: Notification) {
        // Get the Firebase database reference
        val database = FirebaseDatabase.getInstance()
        val userNotificationRef = database.getReference("userNotification").child(username)

        // Generate a unique ID for the notification
        val notificationId = userNotificationRef.push().key

        if (notificationId != null) {
            userNotificationRef.child(notificationId).setValue(notification)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "Notification stored successfully!")
                    } else {
                        Log.e("Firebase", "Error storing notification: ${task.exception?.message}")
                    }
                }
        }
    }

    fun fetchNotificationsFromFirebase(username: String, callback: (List<Notification>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userNotificationRef = database.getReference("userNotification").child(username)

        userNotificationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notifications = mutableListOf<Notification>()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(Notification::class.java)
                    if (notification != null) {
                        notifications.add(notification)
                    }
                }
                callback(notifications)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching notifications: ${error.message}")
            }
        })
    }

    fun clearAllNotifications(context: Context, username: String, adapter: NotificationsAdapter) {
        val database = FirebaseDatabase.getInstance()
        val userNotificationRef = database.getReference("userNotification").child(username)

        userNotificationRef.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "All notifications cleared for $username.")
                    Toast.makeText(context, "All notifications cleared.", Toast.LENGTH_SHORT).show()

                    // Clear local RecyclerView data
                    adapter.updateNotifications(emptyList())
                } else {
                    Log.e("Firebase", "Failed to clear notifications: ${task.exception?.message}")
                    Toast.makeText(context, "Failed to clear notifications.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}