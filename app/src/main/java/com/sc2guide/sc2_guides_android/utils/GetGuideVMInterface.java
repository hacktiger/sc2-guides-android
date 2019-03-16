package com.sc2guide.sc2_guides_android.utils;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class GetGuideVMInterface extends ViewModel{
    private MutableLiveData<List<Guide>> guideList;
    private String error;
    private String lastItemKey;
    private int guideNumLimit = 9;
    private String ref = "guides";
    private String race = " ";
    private String authorId = " ";
    private boolean isComplete;

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getLastItemKey() {
        return lastItemKey;
    }

    public int getGuideNumLimit() {
        return guideNumLimit;
    }

    public String getRef() {
        return ref;
    }

    public LiveData<List<Guide>> getGuides(boolean forceUpdate){
        if(guideList == null || forceUpdate) {
            guideList = new MutableLiveData<>();
            loadGuides();
        }
        if(error != null) {
            return null;
        }
        return guideList;
    }

    public LiveData<List<Guide>> getMoreGuides() {
        loadMoreGuides();
        return guideList;
    }

    public abstract void loadGuides();

    public abstract void loadMoreGuides();

    public void handleLoadGuideLogic(List<Guide> tempListGuide) {
        Collections.reverse(tempListGuide);
        List<Guide> newList = new ArrayList<>();
        for (int i = 0; i < tempListGuide.size() - 1; i++) {
            newList.add(tempListGuide.get(i));
        }
        lastItemKey = tempListGuide.get(tempListGuide.size() - 1).getId();
        // set value to the mutable live data
        guideList.setValue(newList);
    }

    public Guide retrieveDataToModel(DataSnapshot snapshot) throws NotPossibleException {
        // get all the attributes
        String id = snapshot.getKey();
        String title = snapshot.child("title").getValue().toString();
        String my_race = snapshot.child("myRace").getValue().toString();
        String op_race = snapshot.child("opRace").getValue().toString();
        String author_id = snapshot.child("authorId").getValue().toString();
        String author_email = snapshot.child("authorName").getValue().toString();
        String current_time = snapshot.child("date").getValue().toString();
        // list to hold all the guide body items
        List<GuideBodyItem> mList = new ArrayList<>();
        // get guide body items
        for (DataSnapshot mySnap : snapshot.child("guideBodyItems").getChildren()) {
            GuideBodyItem item = new GuideBodyItem(mySnap.child("type").getValue().toString(), mySnap.child("body").getValue().toString());
            mList.add(item);
        }

        return new Guide(id, title, my_race, op_race, author_id, author_email, mList, current_time);
    }

    /**
     * @effects:
     * @param originalList
     */
    public void handleLoadMoreLogic (List<Guide> originalList) {
        Collections.reverse(originalList);

        if (lastItemKey.equals("END")) { // if there are no more to load => return
            return;
        }

        if (originalList.size() < guideNumLimit) { // if the number of guides left are less than the minimum per load
            Objects.requireNonNull(guideList.getValue()).addAll(originalList);
            lastItemKey = "END"; // set up key for no more guide
            return;
        }

        List<Guide> newList = new ArrayList<>();
        for (int i = 0; i < originalList.size() - 1; i++) {
            newList.add(originalList.get(i));
        } // clone the result from query ( except the last element ) to a new list to add to the current list
        lastItemKey = originalList.get(originalList.size() - 1).getId(); // the last element => key for load more
        Objects.requireNonNull(guideList.getValue()).addAll(newList); // add the newly acquired guides to the list
    }
}
