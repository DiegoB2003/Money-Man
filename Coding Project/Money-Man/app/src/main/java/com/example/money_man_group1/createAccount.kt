package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileWriter
import java.io.IOException

class createAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back button functionality
        val prevButton = findViewById<Button>(R.id.backbutton)
        prevButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Next button functionality
        val nextButton = findViewById<Button>(R.id.nextbutton)
        nextButton.setOnClickListener {
            //Saves all data from text boxes to be saved into files
            val firstName = findViewById<EditText>(R.id.firstNameBox).text.toString()
            val lastName = findViewById<EditText>(R.id.lastNameBox).text.toString()
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val password = findViewById<EditText>(R.id.password_toggle).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneNumber).text.toString()
            val yearlyIncome = findViewById<EditText>(R.id.yearlyIncomeText).text.toString().toDouble()
            val dateOfBirth = findViewById<EditText>(R.id.bdayBox).text.toString()

            //Create a Person object with above values and
            //set pie chart values to 0.0 because they didn't spend anything yet
            val person = Person(firstName, lastName, email, password, phoneNumber, yearlyIncome, dateOfBirth,
                food = 0.0, education = 0.0, hobbies = 0.0, health = 0.0, housing = 0.0, other = 0.0 )

            writeToFile(person)//writes object to file

            //Go to service linking page
            val intent = Intent(this, ServiceLinkPage::class.java)
            startActivity(intent)
        }
    }

    private fun writeToFile(person: Person) {
        val file = File(filesDir, "UserData.txt") //creates new file which is saved on emulated device
        try {
            //Creates string with all users data to save
            val data = "${person.firstName}, ${person.lastName}, ${person.email}, ${person.password}, " +
                    "${person.phoneNumber}, ${person.yearlyIncome}, ${person.dateOfBirth}, " +
                    "${person.food}, ${person.education}, ${person.hobbies}, ${person.health}, " +
                    "${person.housing}, ${person.other}\n"

            //Append to the file using FileWriter to not over write previous data
            FileWriter(file, true).use { writer ->
                writer.append(data)  // Append the data to the file
            }
            //Shows notification on phone that data saved in the file
            Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace() //prints out error and shows notification that data didn't save to the file
            Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
