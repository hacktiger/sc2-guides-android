package com.sc2guide.sc2_guides_android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;
import com.sc2guide.sc2_guides_android.error.NotPossibleException;
import com.sc2guide.sc2_guides_android.utils.GetGuideVMInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileViewModel extends GetGuideVMInterface {
    private String authorIdChildName = "authorId";

    @Override
    public void loadGuides() {
        FirebaseDatabase.getInstance()
                .getReference(getRef())
                .orderByChild(authorIdChildName)
                .equalTo(getAuthorId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Guide> tempListGuide = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    tempListGuide.add(retrieveDataToModel(snapshot));
                                } catch (NotPossibleException e) {
                                    e.printStackTrace();
                                }
                            }
                            handleLoadGuideLogic(tempListGuide);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                        // err = databaseError.getMessage();
                        System.err.print(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void loadMoreGuides() {

    }
}
