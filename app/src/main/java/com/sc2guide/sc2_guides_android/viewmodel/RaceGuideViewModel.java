package com.sc2guide.sc2_guides_android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.data.model.GuideBodyItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaceGuideViewModel extends ViewModel {
    private MutableLiveData<List<Guide>> raceGuides;
    private String reference = "guides";

    public LiveData<List<Guide>> getRaceGuides(String race, boolean forceUpdate) {
        if (raceGuides != null && forceUpdate) {
            loadAllGuides(race);
        }
        if (raceGuides == null) {
            raceGuides = new MutableLiveData<>();
            loadAllGuides(race);
        }
        return raceGuides;
    }

    private  void  loadAllGuides(String race) {
        // Do async operation to fetch guides
        FirebaseDatabase.getInstance()
                .getReference(reference)
                .orderByChild("myRace")
                .equalTo(race)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Guide> tempGuidesList = new ArrayList<>();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                try {
                                    String id = snapshot.getKey();
                                    String title = snapshot.child("title").getValue().toString();
                                    String my_race = snapshot.child("myRace").getValue().toString();
                                    String op_race = snapshot.child("opRace").getValue().toString();
                                    String author_id = snapshot.child("authorId").getValue().toString();
                                    String author_email = snapshot.child("authorName").getValue().toString();
                                    String current_time =  snapshot.child("date").getValue().toString();
                                    List<GuideBodyItem> mList = new ArrayList<>();
                                    for(DataSnapshot mySnap : snapshot.child("guideBodyItems").getChildren()){
                                        GuideBodyItem item = new GuideBodyItem(mySnap.child("type").getValue().toString(), mySnap.child("body").getValue().toString());
                                        mList.add(item);
                                    }

                                    tempGuidesList.add(new Guide(id, title,my_race,op_race,author_id,author_email, mList, current_time));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            Collections.reverse(tempGuidesList);
                            raceGuides.setValue(tempGuidesList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }
}
