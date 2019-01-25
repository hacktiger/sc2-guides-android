package com.sc2guide.sc2_guides_android.data.model;

public class GuideBodyItem {
    private String type;
    private String body;

    public GuideBodyItem() {
        //
    }

    public GuideBodyItem(String type, String body) {
        this.type = type;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
