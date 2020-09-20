package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class rsEvent {

    @NotNull
    private String eventname;
    @NotNull
    private String keyword;
    @NotNull
    private int userid;

    public rsEvent(@NotNull String eventname, @NotNull String keyword, @NotNull int userid) {
        this.eventname = eventname;
        this.keyword = keyword;
        this.userid = userid;
    }

    public rsEvent(){

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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
