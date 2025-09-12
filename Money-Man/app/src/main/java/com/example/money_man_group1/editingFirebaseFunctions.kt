package com.example.money_man_group1
import com.google.firebase.database.FirebaseDatabase
import android.util.Log


fun renameFirebaseKey(tableName: String, oldKeyName: String, newKeyName: String) {

    // Get an instance of the Firebase database
        val database = FirebaseDatabase.getInstance()
        // Get a reference to the specified table (node) in the database
        val tableRef = database.getReference(tableName)

        // Define references for the old key and the new key within the specified table
        val oldKeyRef = tableRef.child(oldKeyName)
        val newKeyRef = tableRef.child(newKeyName)


        // Attempt to retrieve the data under the old key
        oldKeyRef.get().addOnSuccessListener { dataSnapshot ->
            // Check if the old key actually exists in the database
            if (dataSnapshot.exists()) {
                // Copy the data under the old key to the new key location
                newKeyRef.setValue(dataSnapshot.value)
                    .addOnSuccessListener {
                        // If the data copy to the new key is successful, delete the old key
                        oldKeyRef.removeValue()
                            .addOnSuccessListener {
                                // Log success message if the old key is deleted successfully
                                Log.d("check","Key renamed successfully in $tableName!")
                            }
                            .addOnFailureListener {
                                // Log an error message if deletion of the old key fails
                                Log.d("check","Failed to delete old key: ${it.message}")
                            }
                    }
                    .addOnFailureListener {
                        // Log an error message if the data copy to the new key fails
                        Log.d("check","Failed to create new key: ${it.message}")
                    }
            } else {
                // Log an informational message if the old key does not exist
                Log.d("check","Old key does not exist in $tableName.")
            }
        }.addOnFailureListener {
            // Log an error message if retrieving data from the old key fails
            Log.d("check","Failed to retrieve data from old key: ${it.message}")
        }
}