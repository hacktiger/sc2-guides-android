package com.sc2guide.sc2_guides_android.data.model;

public class Guide {

    private String title;
    private String body;
    private String authorId;
    private String authorName;
    private String my_race;
    private String op_race;

    public Guide () {
        //
    }

    public Guide(String title, String body, String my_race, String op_race, String authorId, String authorName) {
        this.title = title;
        this.body = body;
        this.my_race = my_race;
        this.op_race = op_race;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMy_race() {
        return my_race;
    }

    public void setMy_race(String my_race) {
        this.my_race = my_race;
    }

    public String getOp_race() {
        return op_race;
    }

    public void setOp_race(String op_race) {
        this.op_race = op_race;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
