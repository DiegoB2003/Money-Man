package com.example.money_man_group1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Date


class NotificationsAdapter(
    private var notificationList: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.notification_message)
        private val timestampTextView: TextView = itemView.findViewById(R.id.notification_timestamp)

        fun bind(notification: Notification) {
            messageTextView.text = notification.message
            timestampTextView.text = Date(notification.timestamp).toString() // Format date if needed

            // Set click listener
            itemView.setOnClickListener { onNotificationClick(notification) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int = notificationList.size

    fun updateNotifications(newList: List<Notification>) {
        notificationList = newList.asReversed()
        notifyDataSetChanged() // Update the RecyclerView
    }
}