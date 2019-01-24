package com.sc2guide.sc2_guides_android.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.sc2guide.sc2_guides_android.error.NotPossibleException;
import com.sc2guide.sc2_guides_android.utils.Converter;

import java.io.Serializable;
import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity
public class Guide implements Serializable {
    @PrimaryKey
    @NonNull private String id;
    private String title;
    @ColumnInfo(name = "authorid")
    private String authorId;
    @ColumnInfo(name = "authorname")
    private String authorName;
    @ColumnInfo(name = "myrace")
    private String myRace;
    @ColumnInfo(name = "oprace")
    private String opRace;
    @ColumnInfo(name = "guidebodyitems")
    @TypeConverters({ Converter.class })
    private List<GuideBodyItem> guideBodyItems;

    private String date;

    public Guide () {
        //
    }

    public List<GuideBodyItem> getGuideBodyItems() {
        return guideBodyItems;
    }

    public void setGuideBodyItems(List<GuideBodyItem> guideBodyItems) {
        this.guideBodyItems = guideBodyItems;
    }

    public Guide(String id, String title, String myRace, String opRace, String authorId, String authorName, List<GuideBodyItem> arr, String date) throws NotPossibleException {
        if (validateId(id) && validateTitle(title) && validateRace(myRace) && validateRace(opRace) && validateAuthorId(authorId) && validateAuthorName(authorName)){
            this.id = id;
            this.title = title;
            this.myRace = myRace;
            this.opRace = opRace;
            this.authorId = authorId;
            this.authorName = authorName;
            this.guideBodyItems = arr;
            this.date = date;
        } else {
            throw new NotPossibleException("Cant create guide. Guide.constructor");
        }
    }

    private boolean validateId(String id) {
        return (!id.isEmpty());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(validateId(id)){
            this.id = id;
        } else {
            throw new IllegalArgumentException("Guide.setId() : id not validated");
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private boolean validateTitle (String title) {
        return (!title.isEmpty());
    }

    private boolean validateRace (String race) {
        return race.equals("Zerg") || race.equals("Protoss") || race.equals("Terran") || race.equals("All");
    }

    private boolean validateAuthorId (String authorId) {
        return (!authorId.isEmpty());
    }

    private boolean validateAuthorName (String authorName) {
        return (!authorName.isEmpty());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (validateTitle(title)) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Guide.setTitle(): Title is not validated");
        }
    }

    public String getMyRace() {
        return myRace;
    }

    public void setMyRace(String myRace) {
        if(validateRace(myRace)){
            this.myRace = myRace;
        } else {
            throw new IllegalArgumentException("Guide.setMyRace(): My Race not validated");
        }
    }

    public String getOpRace() {
        return opRace;
    }

    public void setOpRace(String opRace) {
        if(validateRace(opRace)){
            this.opRace = opRace;
        } else {
            throw new IllegalArgumentException("Guide.setOpRace(): Opponent Race not validated");
        }
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        if (validateTitle(authorId)) {
            this.authorId = authorId;
        } else {
            throw new IllegalArgumentException("Guide.setAuthorId(): Author id is not validated");
        }
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (validateTitle(authorName)) {
            this.authorName = authorName;
        } else {
            throw new IllegalArgumentException("Guide.setAuthorName(): Author name is not validated");
        }
    }
}
