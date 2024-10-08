package com.example.money_man_group1

import android.content.Context
import java.io.File

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
    //Reads in a specific person using their email from files of already created accounts
    fun readPersonByEmail(context: Context, email: String): Person? {
        val file = File(context.filesDir, "UserData.txt")
        if (!file.exists()) return null

        file.useLines { lines ->
            lines.forEach { line ->
                val data = line.split(", ")
                if (data.size == 13 && data[2] == email) { //Checks if email is the one that is logged in
                    return Person( //returns person
                        firstName = data[0],
                        lastName = data[1],
                        email = data[2],
                        password = data[3],
                        phoneNumber = data[4],
                        yearlyIncome = data[5].toDouble(),
                        dateOfBirth = data[6],
                        food = data[7].toDouble(),
                        education = data[8].toDouble(),
                        hobbies = data[9].toDouble(),
                        health = data[10].toDouble(),
                        housing = data[11].toDouble(),
                        other = data[12].toDouble()
                    )
                }
            }
        }
        return null //returns null if no on has that email
    }
}

