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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllGuideViewModel extends ViewModel {
    private MutableLiveData<List<Guide>> allGuides;
    private String reference = "guides";

    public LiveData<List<Guide>> getAllGuides(boolean forceUpdate) {
        if (forceUpdate) {
            Log.d("QQPP", "AllguidViewModel.getAllGuide().forceUpdate");

            loadAllGuides();
        }
        // TODO: pagination
        if (allGuides == null) {
            Log.d("QQPP", "AllguidViewModel.getAllGuide().all == null");

            allGuides = new MutableLiveData<>();
            loadAllGuides();
        }
        return allGuides;
    }

    private  void  loadAllGuides() {
        Log.d("QQPP", "AllguidViewModel.loadAllGuides()");
        // Do async operation to fetch guides
        FirebaseDatabase.getInstance()
                .getReference(reference)
                .orderByChild("currentTime")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Guide> tempListGuide = new ArrayList<>();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                try {
                                    // get all the attributes
                                    String title = snapshot.child("title").getValue().toString();
                                    String my_race = snapshot.child("myRace").getValue().toString();
                                    String op_race = snapshot.child("opRace").getValue().toString();
                                    String author_id = snapshot.child("authorId").getValue().toString();
                                    String author_email = snapshot.child("authorName").getValue().toString();
                                    long current_time = (long) snapshot.child("currentTime").getValue();
                                    // list to hold all the guide body items
                                    List<GuideBodyItem> mList = new ArrayList<>();
                                    // get guide body items
                                    for(DataSnapshot mySnap : snapshot.child("guideBodyItems").getChildren()){
                                        GuideBodyItem item = new GuideBodyItem(mySnap.child("type").getValue().toString(), mySnap.child("body").getValue().toString());
                                        mList.add(item);
                                    }
                                    Log.d("QQPP", "WOAHHHHH");

                                    // add the snapshot to the temp list
                                    tempListGuide.add(new Guide(title,my_race,op_race,author_id,author_email, mList, current_time));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            // reverse the temp list
                            Collections.reverse(tempListGuide);
                            Log.d("QQPP", "soze :" + tempListGuide.size());

                            // set value to the mutable live data
                            allGuides.setValue(tempListGuide);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }
}
