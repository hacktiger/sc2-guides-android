package com.sc2guide.sc2_guides_android.viewmodel;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.error.NotPossibleException;
import com.sc2guide.sc2_guides_android.utils.GetGuideVMInterface;

import java.util.ArrayList;
import java.util.List;

public class AllGuideViewModel extends GetGuideVMInterface {
    @Override
    public void loadGuides() {
        FirebaseDatabase.getInstance()
                .getReference(getRef())
                .orderByKey()
                .limitToLast(getGuideNumLimit())
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
                            // reverse the temp list
                            handleLoadGuideLogic(tempListGuide);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }

    @Override
    public void loadMoreGuides() {
        FirebaseDatabase.getInstance()
                .getReference(getRef())
                .orderByKey()
                .limitToLast(getGuideNumLimit())
                .endAt(getLastItemKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Guide> tempListGuide = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // add the snapshot to the temp list
                                try {
                                    tempListGuide.add(retrieveDataToModel(snapshot));
                                } catch (NotPossibleException e) {
                                    e.printStackTrace();
                                }
                            }
                            // set value to the mutable live data
                            handleLoadMoreLogic(tempListGuide);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }
}
