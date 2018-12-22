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

import java.util.ArrayList;
import java.util.List;

public class RaceGuideViewModel extends ViewModel {
    private MutableLiveData<List<Guide>> raceGuides;
    private String reference = "guides";

    public LiveData<List<Guide>> getRaceGuides(String race) {
        if (raceGuides == null) {
            raceGuides = new MutableLiveData<List<Guide>>();
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
                            List<Guide> listGuides = new ArrayList<>();
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                String title = snapshot.child("title").getValue().toString();
                                String body = snapshot.child("body").getValue().toString();
                                String my_race = snapshot.child("myRace").getValue().toString();
                                String op_race = snapshot.child("opRace").getValue().toString();
                                String author_id = snapshot.child("authorId").getValue().toString();
                                String author_email = snapshot.child("authorName").getValue().toString();

                                try {
                                    listGuides.add(new Guide(title,body,my_race,op_race,author_id,author_email));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            raceGuides.setValue(listGuides);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.getCode();
                    }
                });
    }
}
