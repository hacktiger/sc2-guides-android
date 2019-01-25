package com.sc2guide.sc2_guides_android.controller;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.data.model.User;
import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.service.FirebaseDatabaseService;
import com.sc2guide.sc2_guides_android.service.FirebaseStorageService;

public class FirebaseController {
    // auth
    private FirebaseAuthService mFirebaseAuthService;
    // Storage
    private FirebaseStorageService mFirebaseStorageService;

    private StorageReference profileImgStorageRef;
    // database
    private FirebaseDatabaseService mFirebaseDbService;
    private String guideRefName = "guides";
    private String userRefName = "users";

    private DatabaseReference guideDBRef;
    private DatabaseReference userDBRef;

    public FirebaseController () {
        mFirebaseAuthService = new FirebaseAuthService();
        mFirebaseDbService = new FirebaseDatabaseService();
        mFirebaseStorageService = new FirebaseStorageService();
    }
    // Authentication related methods
    public void createUserWithEmailAndPassword(String email, String password,
                                               OnCompleteListener<AuthResult> onCompleteListener, OnFailureListener onFailureListener){
        mFirebaseAuthService.getFirebase().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public void signInWithEmailAndPassword(String email, String password,
                                           OnCompleteListener<AuthResult> onCompleteListener, OnFailureListener onFailureListener) {
        mFirebaseAuthService.getFirebase().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    public FirebaseUser currentUser() {
        return mFirebaseAuthService.currentUser();
    }

    public void insertGuide (Guide guide, OnCompleteListener<Void> onCompleteListener) {
        guideDBRef = mFirebaseDbService.getReference(guideRefName);
        DatabaseReference ref = guideDBRef.push();
        String key = ref.getKey();
        guide.setId(key);
        ref.setValue(guide).addOnCompleteListener(onCompleteListener);
    }

    public void insertUser (User user, OnCompleteListener<Void> onCompleteListener) {
        userDBRef = mFirebaseDbService.getReference(userRefName);
        userDBRef.push().setValue(user).addOnCompleteListener(onCompleteListener);
    }

    public void updateUser(User user) {
        // TODO: implement method
    }

    /**
     * @effects: handles upload user profile picture to firebase storage
     *              create profileImg storage reference
     *              delete old profile pic if any
     *              upload new profile pic
     * @param filePath
     * @param userId
     * @param onProgressListener
     * @param onFailureListener
     * @param onSuccessListener
     */
    public void insertProfilePic (Uri filePath, String userId,
                                  OnProgressListener<UploadTask.TaskSnapshot> onProgressListener, OnFailureListener onFailureListener,
                                  OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        profileImgStorageRef = mFirebaseStorageService.getStorageReference().child("images/"+ userId);
        // Delete old image if there is
        profileImgStorageRef.delete()
                .addOnFailureListener(onFailureListener);
        // Upload new image
        profileImgStorageRef.putFile(filePath)
                .addOnSuccessListener(onSuccessListener)
                .addOnProgressListener(onProgressListener)
                .addOnFailureListener(onFailureListener);
    }
}
