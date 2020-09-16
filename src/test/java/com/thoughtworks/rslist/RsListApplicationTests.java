package com.thoughtworks.rslist;

import com.thoughtworks.rslist.domain.rsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

//import static com.thoughtworks.rslist.api.RsController.initial_rsEvent;
import static com.thoughtworks.rslist.api.RsController.initial_rsEvent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void setup(){
        initial_rsEvent();
    }
    @Test
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
    public void should_add_rsevent_to_relist() throws Exception {
        String jsonString = "{\"eventname\":\"猪肉涨价了\",\"keyword\":\"经济\"}";
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventname", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[3].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyword", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_update_a_rsevent() throws Exception {
        String jsonString = "{\"eventname\":\"猪肉涨价了\",\"keyword\":\"经济\"}";
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
    public void should_update_rsevent_when_only_change_one_of_the_feature() throws Exception {
        String jsonString = "{\"eventname\":\"猪肉涨价了\"}";
        mockMvc.perform(patch("/rs/1/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
        String jsonString1 = "{\"eventname\":\"人肉涨价了\"}";
        mockMvc.perform(patch("/rs/2/event").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
        String jsonString2 = "{\"keyword\":\"别吃\"}";
        mockMvc.perform(patch("/rs/1/event").content(jsonString2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("别吃")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
        String jsonString3 = "{\"keyword\":\"快买\"}";
        mockMvc.perform(patch("/rs/2/event").content(jsonString3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("别吃")))
                .andExpect(jsonPath("$[1].eventname", is("人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("快买")))
                .andExpect(jsonPath("$[2].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_delete_one_rsevent() throws Exception {
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventname", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/rs/2")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventname", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(0))).andExpect(status().isOk());


    }
}
