package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {

    @NotNull
    private String eventname;
    @NotNull
    private String keyword;
    @NotNull
    private int userid;
//    @NotNull
//    private int voteNum = 0;
//    @JsonIgnore
//    public void setVoteNum(int voteNum) {
//        this.voteNum = voteNum;
//    }
//    @JsonProperty
//    public int getVoteNum() {
//        return voteNum;
//    }

}
