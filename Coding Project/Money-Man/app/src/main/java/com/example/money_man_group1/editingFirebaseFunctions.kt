package com.example.money_man_group1
import com.google.firebase.database.FirebaseDatabase


fun renameFirebaseKey(tableName: String, oldKeyName: String, newKeyName: String) {
    // Get a reference to the specified table in the Firebase database
    val database = FirebaseDatabase.getInstance()
    val tableRef = database.getReference(tableName)

    // References to the old and new keys within the specified table
    val oldKeyRef = tableRef.child(oldKeyName)
    val newKeyRef = tableRef.child(newKeyName)

    // Retrieve the data under the old key
    oldKeyRef.get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            // Write the data under the new key
            newKeyRef.setValue(dataSnapshot.value)
                .addOnSuccessListener {
                    // Delete the old key after successful write
                    oldKeyRef.removeValue()
                        .addOnSuccessListener {
                            println("Key renamed successfully in $tableName!")
                        }
                        .addOnFailureListener {
                            println("Failed to delete old key: ${it.message}")
                        }
                }
                .addOnFailureListener {
                    println("Failed to create new key: ${it.message}")
                }
        } else {
            println("Old key does not exist in $tableName.")
        }
    }.addOnFailureListener {
        println("Failed to retrieve data from old key: ${it.message}")
    }
}