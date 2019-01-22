package com.sc2guide.sc2_guides_android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
            loadAllGuides();
        }

        if (allGuides == null) {
            allGuides = new MutableLiveData<>();
            loadAllGuides();
        }

        return allGuides;
    }

    private  void  loadAllGuides() {
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
                                    String id = snapshot.getKey();
                                    String title = snapshot.child("title").getValue().toString();
                                    String my_race = snapshot.child("myRace").getValue().toString();
                                    String op_race = snapshot.child("opRace").getValue().toString();
                                    String author_id = snapshot.child("authorId").getValue().toString();
                                    String author_email = snapshot.child("authorName").getValue().toString();
                                    String current_time =  snapshot.child("date").getValue().toString();
                                    // list to hold all the guide body items
                                    List<GuideBodyItem> mList = new ArrayList<>();
                                    // get guide body items
                                    for(DataSnapshot mySnap : snapshot.child("guideBodyItems").getChildren()){
                                        GuideBodyItem item = new GuideBodyItem(mySnap.child("type").getValue().toString(), mySnap.child("body").getValue().toString());
                                        mList.add(item);
                                    }
                                    // add the snapshot to the temp list
                                    tempListGuide.add(new Guide(id,title,my_race,op_race,author_id,author_email, mList, current_time));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            // reverse the temp list
                            Collections.reverse(tempListGuide);
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
