package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@Table(name="user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserPO {
    @Id
    @GeneratedValue(generator = "userPO_id",strategy = GenerationType.AUTO)
    //@Column(name = "usesr_id")
    private int id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int votenumber=10;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;
}
