package com.example.money_man_group1

data class UserNotifications(
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)