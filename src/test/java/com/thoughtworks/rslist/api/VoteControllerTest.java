package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    RsEventPO rsEventPO;
    UserPO userPO;
    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
        userPO = UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build();
        userRepository.save(userPO);
        RsEvent rsEvent = RsEvent.builder().eventname("djn牌猪肉涨价了").keyword("经济").userid(userPO.getId()).voteNum(0).build();
        rsEventPO = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
                .userPO(userPO).voteNum(0).build();
        rsEventRepository.save(rsEventPO);
    }
    @Test
    public void should_pageble5_the_record_when_record_a_lot() throws Exception {
        for(int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO)
                    .rsEvent(rsEventPO).localDateTime(LocalDateTime.now()).num(i+1).build();
            voteRepository.save(votePO);
        }
        mockMvc.perform(get("/voteRecord").param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventPO.getId()))
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));

        mockMvc.perform(get("/voteRecord").param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventPO.getId()))
                .param("pageIndex","2"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));

    }
    @Test
    public void should_return_all_the_record_when_give_a_priod_of_time() throws Exception {
        //准备测试数据
        for(int i = 0; i < 8; i++){
            VotePO votePO = VotePO.builder().user(userPO)
                    .rsEvent(rsEventPO).localDateTime(LocalDateTime.now()).num(i+1).build();
            voteRepository.save(votePO);
        }
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 13, 9, 43, 20);
        VotePO votePO = VotePO.builder().user(userPO)
                .rsEvent(rsEventPO).localDateTime(localDateTime).num(5).build();
        voteRepository.save(votePO);
        //测试
        LocalDateTime startTime = LocalDateTime.of(2019, 1, 13, 9, 43, 20);
        LocalDateTime endTime = LocalDateTime.of(2021, 1, 13, 9, 43, 20);
        mockMvc.perform(get("/voteRecord").param("startTime",String.valueOf(startTime))
                .param("endTime",String.valueOf(endTime))
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));
        mockMvc.perform(get("/voteRecord").param("startTime",String.valueOf(startTime))
                .param("endTime",String.valueOf(endTime))
                .param("pageIndex","2"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));
        LocalDateTime startTime1 = LocalDateTime.of(2017, 1, 13, 9, 43, 20);
        LocalDateTime endTime1 = LocalDateTime.of(2019, 1, 13, 9, 43, 20);
        mockMvc.perform(get("/voteRecord").param("startTime",String.valueOf(startTime1))
                .param("endTime",String.valueOf(endTime1))
                .param("pageIndex","1"))
                .andExpect(jsonPath("$",hasSize(1)));
    }
}