package com.example.circol;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseProvider {

    public static FirebaseDatabase get() {
        return FirebaseDatabase.getInstance();
    }
}
