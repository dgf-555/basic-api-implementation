package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @ManyToOne
    private UserPO userPO;
}
