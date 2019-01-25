package com.sc2guide.sc2_guides_android.service;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageService {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseStorageService () {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
