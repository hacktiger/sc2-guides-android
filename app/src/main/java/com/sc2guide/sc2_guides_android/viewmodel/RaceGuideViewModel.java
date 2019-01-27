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

public class RaceGuideViewModel extends GetGuideVMInterface {

    @Override
    public void loadGuides() {
        setComplete(false);
        // Do async operation to fetch guides
        FirebaseDatabase.getInstance()
                .getReference(getRef())
                .orderByChild("myRace")
                .equalTo(getRace())
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
                            handleLoadGuideLogic(tempListGuide);
                            setComplete(true);

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
        setComplete(false);

        FirebaseDatabase.getInstance()
                .getReference(getRef())
                .orderByChild("myRace")
                .equalTo(getRace())
                .limitToLast(getGuideNumLimit())
                // .endAt(getLastItemKey())  TODO: !important : err af, need some other solution
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
                            handleLoadMoreLogic(tempListGuide);
                            setComplete(true);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }

}
