package com.thoughtworks.rslist.domain;

public class rsEvent {
    private String eventname;
    private String keyword;

    public rsEvent(String eventname, String keyword) {
        this.eventname = eventname;
        this.keyword = keyword;
    }
    public rsEvent(){

    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
