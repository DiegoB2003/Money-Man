package com.example.money_man_group1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        firebaseReference = FirebaseDatabase.getInstance().getReference("Users")
        // Next button functionality
        val nextButton = findViewById<Button>(R.id.nextbutton)
        binding.nextbutton.setOnClickListener {
            //Commented out until I finish soon :)
//                firebaseReference.setValue("User Information")
//                    .addOnCompleteListener{
//                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
//                    }
                // Go to service linking page
                val intent = Intent(this, ServiceLinkPage::class.java)
                startActivity(intent)
        }
    }
}
