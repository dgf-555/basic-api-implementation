package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "rsevent")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventPO {
    @Id
    @GeneratedValue(generator = "rsEventPO_id",strategy = GenerationType.AUTO)
    private int id;
    private String eventname;
    private String keyword;
    private int voteNum = 0;
    @ManyToOne(targetEntity = UserPO.class)
    private UserPO userPO;

    @JsonBackReference
    public UserPO getUserPO() {
        return userPO;
    }
    @JsonBackReference
    public void setUserPO(UserPO userPO) {
        this.userPO = userPO;
    }
}