package com.thoughtworks.rslist;


import com.thoughtworks.rslist.api.RsController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.User;

import com.thoughtworks.rslist.domain.rsEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

//import org.springframework.test.web.servlet.MockMvcBuilder;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.rslist.api.RsController.initial_rsEvent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }
//    @BeforeEach
//    public void setup(){
//        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
//    }
    @Test
    @Order(1)
    public void should_get_rs_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void should_get_one_rsevent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventname", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventname", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventname", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void should_get_rslist_from_start_to_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void should_update_a_rsevent() throws Exception {
        User user = new User("dgf","male",19,"a@b.com","18888888888");
        rsEvent rsEvent = new rsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        //String jsonString = "{\"eventname\":\"猪肉涨价了\",\"keyword\":\"经济\"}";
        mockMvc.perform(patch("/rs/1/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/rs/2/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/rs/3/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
    }
    @Test
    @Order(5)
    public void should_update_rsevent_when_only_change_one_of_the_feature() throws Exception {
        User user = new User("dgf","male",19,"a@b.com","18888888888");
//        rsEvent rsEvent = new rsEvent("猪肉涨价了",null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonString = objectMapper.writeValueAsString(rsEvent);
//        //String jsonString = "{\"eventname\":\"猪肉涨价了\"}";
//        mockMvc.perform(patch("/rs/1/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[0].keyword", is("经济")))
//                .andExpect(jsonPath("$[1].eventname", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[1].keyword", is("经济")))
//                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[2].keyword", is("经济")))
//                .andExpect(status().isOk());
        rsEvent rsEvent1 = new rsEvent("人肉涨价了",null,user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString1 = objectMapper.writeValueAsString(rsEvent1);
        //String jsonString1 = "{\"eventname\":\"人肉涨价了\"}";
        mockMvc.perform(patch("/rs/2/event").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
        rsEvent rsEvent2 = new rsEvent(null,"别吃",user);
        //ObjectMapper objectMapper = new ObjectMapper();
        String jsonString2 = objectMapper.writeValueAsString(rsEvent2);
        //String jsonString2 = "{\"keyword\":\"别吃\"}";
        mockMvc.perform(patch("/rs/1/event").content(jsonString2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("别吃")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
        rsEvent rsEvent3 = new rsEvent(null,"快买",user);
        //ObjectMapper objectMapper = new ObjectMapper();
        String jsonString3 = objectMapper.writeValueAsString(rsEvent3);
        //String jsonString3 = "{\"keyword\":\"快买\"}";
        mockMvc.perform(patch("/rs/2/event").content(jsonString3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("别吃")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("快买")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
    }
    @Test
    @Order(6)
    public void should_add_rsevent_to_relist() throws Exception {
        User user = new User("dgf","male",19,"a@b.com","18888888888");
        rsEvent rsEvent = new rsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        //String jsonString = "{\"eventname\":\"猪肉涨价了\",\"keyword\":\"经济\"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("别吃")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("快买")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(jsonPath("$[3].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")))
                .andExpect(status().isOk());
    }
    @Test
    @Order(7)
    public void should_delete_one_rsevent() throws Exception {
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("快买")))
                .andExpect(jsonPath("$[1].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济"))).andExpect(status().isOk());
    }
    //本次作业新需求
    @Test
    @Order(8)
    public void should_add_user_when_user_not_in_userlist() throws Exception {
        User user = new User("DGF","male",19,"a@b.com","18888888888");
        rsEvent rsEvent = new rsEvent("遇到困难","奥利给",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        //String jsonString = "{\"eventname\":\"猪肉涨价了\",\"keyword\":\"经济\"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("遇到困难")))
                .andExpect(jsonPath("$[1].keyword", is("奥利给")))
                .andExpect(status().isOk());
    }
}
