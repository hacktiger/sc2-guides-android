package com.sc2guide.sc2_guides_android.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.error.NotPossibleException;
import com.sc2guide.sc2_guides_android.utils.GetGuideVMInterface;

import java.util.ArrayList;
import java.util.List;

public class AllGuideViewModel extends GetGuideVMInterface {
    private boolean isComplete;
    @Override
    public void loadGuides() {
        isComplete = false;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getRef());
        ref.orderByKey()
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
                            isComplete = true;

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }

    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void loadMoreGuides() {
        isComplete = false;
        Log.d("ZZLL", "change is complete 11  " + isComplete);

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
                            Log.d("ZZLL", "---------------------------------------------------");
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    Log.d("ZZLL", snapshot.child("title").getValue().toString());
                                    tempListGuide.add(retrieveDataToModel(snapshot));
                                } catch (NotPossibleException e) {
                                    e.printStackTrace();
                                }
                            }
                            handleLoadMoreLogic(tempListGuide);
                            isComplete = true;
                            Log.d("ZZLL", "change is complete 2  " + isComplete);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }
}
