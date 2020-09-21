package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePO {
    @Id
    @GeneratedValue(generator = "votePO_id",strategy = GenerationType.AUTO)
    private int id;
    private int num;
    private int userid;
    private LocalDateTime votetime;
    @ManyToOne(targetEntity = UserPO.class)
    @JoinColumn(name = "user_id")
    private UserPO userPO;
    @ManyToOne(targetEntity = RsEventPO.class)
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEventPO;



}
