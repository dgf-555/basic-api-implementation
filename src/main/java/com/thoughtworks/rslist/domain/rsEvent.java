package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class rsEvent {
    private String eventname;
    private String keyword;
    private User user;

    public rsEvent(String eventname, String keyword, User user) {
        this.eventname = eventname;
        this.keyword = keyword;
        this.user = user;
    }
    public rsEvent(){

    }
    @JsonIgnore
    public User getUser() {
        return user;
    }
    @JsonProperty
    public void setUser(User user) {
        this.user = user;
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
