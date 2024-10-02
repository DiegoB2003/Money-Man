package com.example.money_man_group1

data class Person(
    //Users personal information to be saved
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val yearlyIncome: Double,
    val dateOfBirth: String,

    //PieChart values for spending categories
    val food: Double,
    val education: Double,
    val hobbies: Double,
    val health: Double,
    val housing: Double,
    val other: Double
)
//Functions for getting user data out of files
class PersonData {

}

