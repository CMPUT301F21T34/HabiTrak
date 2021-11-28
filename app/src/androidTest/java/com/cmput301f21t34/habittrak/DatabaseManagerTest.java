package com.cmput301f21t34.habittrak;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-11-03
 * @see DatabaseManager
 */
public class DatabaseManagerTest {

    private static DatabaseManager dm;
    private static FirebaseFirestore db;

    /*
    @BeforeClass
    public static void setUpDatabase() {
        dm = new DatabaseManager();
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.useEmulator("10.0.2.2", 8080);
            FirebaseFirestoreSettings settings =
                    new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
            db.setFirestoreSettings(settings);
        } catch (IllegalStateException ignored) {
        }
    }

    @Before
    public void setUp() {
        this.db = dm.getDatabase();
    }*/

    /**
     * createNewUser
     *
     * @author Henry
     */
    @Test
    public void createNewUserTest() {
        dm = new DatabaseManager();
        db = dm.getDatabase();
        String email = "test@gmail.com";
        String username = "testUser";
        dm.createNewUser(email, username);

        try {
            DocumentReference docref = db.collection("users").document("test@gmail.com");
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();
            if (document.getData() != null) {
                assertEquals(username, document.get("Username"));
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}

