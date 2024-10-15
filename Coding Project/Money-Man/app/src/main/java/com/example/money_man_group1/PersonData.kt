package com.example.money_man_group1

abstract class PersonData {
    //Users personal information to be saved
    abstract val firstName: String
    abstract val lastName: String
    abstract val email: String
    abstract val password: String
    abstract val phoneNumber: String
    abstract val yearlyIncome: Double
    abstract val dateOfBirth: String

    //PieChart values for spending categories
    abstract val food: Double
    abstract val education: Double
    abstract val hobbies: Double
    abstract val health: Double
    abstract val housing: Double
    abstract val other: Double
}

