package com.sc2guide.sc2_guides_android.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthService {
    private FirebaseAuth firebase;
    private FirebaseUser user;

    public FirebaseAuthService() {
        firebase = FirebaseAuth.getInstance();
        user = firebase.getCurrentUser();
    }

    public FirebaseUser currentUser() {
        return user;
    }

    public FirebaseAuth getFirebase () {
        return firebase;
    }

    public void signOut () { firebase.signOut(); }


}
