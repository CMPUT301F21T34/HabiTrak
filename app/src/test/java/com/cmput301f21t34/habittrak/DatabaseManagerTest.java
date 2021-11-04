package com.cmput301f21t34.habittrak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_Event;
import com.cmput301f21t34.habittrak.user.User;
import com.cmput301f21t34.habittrak.DatabaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-11-03
 * @see DatabaseManager
 * @see Database_Pointer
 */
public class DatabaseManagerTest {

    /**
     * mockUser
     *
     * @return User
     * Returns a new User object to perform reading from and writing to the databasae
     */
    private User mockUser(String email, String username, String password) {
        User mockUser = new User(email);
        mockUser.setUsername(username);
        mockUser.setPassword(password);
        return mockUser;
    }

    /**
     * createNewUser
     *
     * @author Henry
     */
    // NOT WORKING
    @Test
    public void createNewUser() {
        /*
        User user = mockUser("test1@gmail.com", "test1", "12345");
        DatabaseManager db = new DatabaseManager();
        db.createNewUser(user.getEmail(), user.getUsername(), user.getPassword(), user.getBiography());

        String TAG = "New User";

        DocumentReference docRef = db.getDatabase().collection("users").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Found document");
                        assertEquals(document.get("username").toString(), user.getUsername());
                        assertEquals(document.get("password").toString(), user.getPassword());
                        assertEquals(document.getId().toString(), user.getEmail());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

         */
    }
}

