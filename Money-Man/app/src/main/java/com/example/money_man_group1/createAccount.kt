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
import com.example.money_man_group1.databinding.ActivityCreateAccountBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class createAccount : AppCompatActivity() {

    lateinit var binding : ActivityCreateAccountBinding //Binding for the activity to xml file
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityCreateAccountBinding.inflate(layoutInflater) //Initialize the binding
        setContentView(binding.root) //Set the content view to the root view of the binding

        // Back button functionality
        val prevButton = findViewById<Button>(R.id.backbutton)
        prevButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Next button functionality
        val nextButton = findViewById<Button>(R.id.nextbutton)
        binding.nextbutton.setOnClickListener {
            //Saves all data from text boxes to be saved into files
            val firstName = findViewById<EditText>(R.id.firstNameBox).text.toString()
            val lastName = findViewById<EditText>(R.id.lastNameBox).text.toString()
            val username = findViewById<EditText>(R.id.usernameBox).text.toString()
            val password = findViewById<EditText>(R.id.password_toggle).text.toString()
            val pass2 = findViewById<EditText>(R.id.password_toggle2).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneNumber).text.toString()
            val yearlyIncome = findViewById<EditText>(R.id.yearlyIncomeText).text.toString()
            val dateOfBirth = findViewById<EditText>(R.id.bdayBox).text.toString()

            //Makes sure no text box is empty and passwords boxed match
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() ||
                phoneNumber.isEmpty() || yearlyIncome.isEmpty() || dateOfBirth.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                if (password != pass2) { //passwords do not match don't continue
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val income = yearlyIncome.toDouble() //Converts income to double to put into UserData

                val person = UserData(firstName, lastName, username, password, phoneNumber, income, dateOfBirth)
                firebaseReference = FirebaseDatabase.getInstance().getReference("Users") //Gets reference to firebase database
                firebaseReference.child(username).setValue(person) //Puts data into firebase database and separated by username
                    .addOnCompleteListener { //If data is saved successfully says in app
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()

                        val userInfo = UserSpendingInfo()

                        //now we simply create a row for the user on the userSpendingInfo table
                        firebaseReference = FirebaseDatabase.getInstance().getReference("userSpendingInfo")
                        firebaseReference.child(username).setValue(userInfo)

                        //create a row for the user on the userActivityLog table
                        firebaseReference = FirebaseDatabase.getInstance().getReference("userActivityLog")
                        firebaseReference.child(username).setValue("")

                        //create a row for the user on the userNotification table
                        firebaseReference = FirebaseDatabase.getInstance().getReference("userNotification")
                        firebaseReference.child(username).setValue("")

                    }
                // Go to service linking page
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}