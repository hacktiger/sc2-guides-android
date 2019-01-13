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

    public LiveData<List<Guide>> getAllGuides() {
        if (allGuides == null) {
            allGuides = new MutableLiveData<>();
            loadAllGuides();
        }

        return allGuides;
    }

    // TODO: listen to child addded
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
                                String title = snapshot.child("title").getValue().toString();
                                String body = snapshot.child("body").getValue().toString();
                                String my_race = snapshot.child("myRace").getValue().toString();
                                String op_race = snapshot.child("opRace").getValue().toString();
                                String author_id = snapshot.child("authorId").getValue().toString();
                                String author_email = snapshot.child("authorName").getValue().toString();
                                long current_time = (long) snapshot.child("currentTime").getValue();

                                List<GuideBodyItem> mList = new ArrayList<>();

                                for(DataSnapshot mySnap : snapshot.child("guideBodyItems").getChildren()){
                                    GuideBodyItem item = new GuideBodyItem(mySnap.child("type").toString(), mySnap.child("body").toString());
                                    mList.add(item);
                                }

                                try {
                                    tempListGuide.add(new Guide(title,body,my_race,op_race,author_id,author_email, mList, current_time));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            Collections.reverse(tempListGuide);

                            allGuides.setValue(tempListGuide);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //
                        databaseError.getCode();
                    }
                });
    }
}
