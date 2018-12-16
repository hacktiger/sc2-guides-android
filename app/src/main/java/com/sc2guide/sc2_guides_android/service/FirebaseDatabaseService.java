package com.sc2guide.sc2_guides_android.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseService {
    private FirebaseDatabase mDatabase;

    public FirebaseDatabaseService () {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public DatabaseReference getReference (String ref) {
        return mDatabase.getReference(ref);
    }
}
