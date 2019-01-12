package com.sc2guide.sc2_guides_android.data.model;

import java.io.Serializable;
import java.util.List;

public class Guide implements Serializable {

    private String title;
    private String body;
    private String authorId;
    private String authorName;
    private String myRace;
    private String opRace;
    private List<GuideBodyItem> guideBodyItems;

    public Guide () {
        //
    }

    public List<GuideBodyItem> getGuideBodyItems() {
        return guideBodyItems;
    }

    public void setGuideBodyItems(List<GuideBodyItem> guideBodyItems) {
        this.guideBodyItems = guideBodyItems;
    }

    public Guide(String title, String body, String myRace, String opRace, String authorId, String authorName, List<GuideBodyItem> arr) throws Exception{
        if (validateTitle(title) && validateRace(myRace) && validateRace(opRace) && validateAuthorId(authorId) && validateAuthorName(authorName)){
            this.title = title;
            this.body = body;
            this.myRace = myRace;
            this.opRace = opRace;
            this.authorId = authorId;
            this.authorName = authorName;
            this.guideBodyItems = arr;
        } else {
            throw new Exception("Cant create guide. Guide.constructor");
        }
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
            throw new IllegalArgumentException("Title is not validated");
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (validateTitle(body)) {
            this.body = body;
        } else {
            throw new IllegalArgumentException("Body is not validated");
        }
    }

    public String getMyRace() {
        return myRace;
    }

    public void setMyRace(String myRace) {
        if(validateRace(myRace)){
            this.myRace = myRace;
        } else {
            throw new IllegalArgumentException("My Race not validated");
        }
    }

    public String getOpRace() {
        return opRace;
    }

    public void setOpRace(String opRace) {
        if(validateRace(opRace)){
            this.opRace = opRace;
        } else {
            throw new IllegalArgumentException("Opponent Race not validated");
        }
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        if (validateTitle(authorId)) {
            this.authorId = authorId;
        } else {
            throw new IllegalArgumentException("Author id is not validated");
        }
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (validateTitle(authorName)) {
            this.authorName = authorName;
        } else {
            throw new IllegalArgumentException("Author name is not validated");
        }
    }


}
