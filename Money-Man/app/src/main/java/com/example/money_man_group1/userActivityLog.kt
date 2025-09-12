package com.example.money_man_group1

data class UserActivityLog(
    val message: String = "",
    val amount: Double = 0.0,
    val category : String = "",
    val timestamp: Long = System.currentTimeMillis()
)