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
import com.example.money_man_group1.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    companion object { var userData: UserData? = null } //Companion object to hold user data and make it available to other activities
    lateinit var binding : ActivityMainBinding //Binding for the activity to xml file
    private lateinit var firebaseReference: DatabaseReference //reference to firebase database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater) //Initialize the binding
        setContentView(binding.root) //Set the content view to the root view of the binding

        val loginButton = findViewById<Button>(R.id.loginScreenButton)

        loginButton.setOnClickListener {
            val user = findViewById<EditText>(R.id.editTextUsername).text.toString()
            val pass = findViewById<EditText>(R.id.editTextPassword).text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                firebaseReference = FirebaseDatabase.getInstance().getReference("Users") // Gets reference to firebase database
                firebaseReference.child(user).get().addOnSuccessListener {
                    dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        //Retrieve userdata from the database
                        val firstName = dataSnapshot.child("firstName").getValue(String::class.java)!!
                        val lastName = dataSnapshot.child("lastName").getValue(String::class.java)!!
                        val password = dataSnapshot.child("password").getValue(String::class.java)!!
                        val phoneNumber = dataSnapshot.child("phoneNumber").getValue(String::class.java)!!
                        val yearlyIncome = dataSnapshot.child("yearlyIncome").getValue(Double::class.java)!!
                        val dateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String::class.java)!!

                        if (password == pass) { //Make sure password matches
                            //Save user data in the companion object
                            userData = UserData(firstName, lastName, user, password, phoneNumber, yearlyIncome, dateOfBirth)
                            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show() //Login works
                            val intent = Intent(this, BudgetPage::class.java) //Start next activity when login works
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
        }

        val createAccountButton = findViewById<Button>(R.id.createAccountScreenButton)
        createAccountButton.setOnClickListener { //goes to create account screen
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
        }

    }
}