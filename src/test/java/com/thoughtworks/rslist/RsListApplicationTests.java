package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    ObjectMapper objectMapper;
    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    public void should_get_rs_list() throws Exception {
        //创建用户并添加
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        //创建热搜并添加
        rsEvent rsEvent = new rsEvent("djn牌狗肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO);
        rsEvent rsEvent1 = new rsEvent("djn牌人肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO1 = RsEventPO.builder().eventname(rsEvent1.getEventname()).keyword(rsEvent1.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO1);
        //测试
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("djn牌狗肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("djn牌人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_one_rsevent() throws Exception {
        //创建用户并添加
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        //创建热搜并添加
        rsEvent rsEvent = new rsEvent("djn牌猪肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO);
        rsEvent rsEvent1 = new rsEvent("djn牌人肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO1 = RsEventPO.builder().eventname(rsEvent1.getEventname()).keyword(rsEvent1.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO1);
        //测试
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventname", is("djn牌猪肉涨价了")))
                .andExpect(jsonPath("$.keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventname", is("djn牌人肉涨价了")))
                .andExpect(jsonPath("$.keyword", is("经济")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_get_rslist_from_start_to_end() throws Exception {
        //创建用户并添加
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        //创建热搜并添加
        rsEvent rsEvent = new rsEvent("djn牌猪肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO);
        rsEvent rsEvent1 = new rsEvent("djn牌人肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO1 = RsEventPO.builder().eventname(rsEvent1.getEventname()).keyword(rsEvent1.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO1);
        rsEvent rsEvent2 = new rsEvent("djn牌狗肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO2 = RsEventPO.builder().eventname(rsEvent2.getEventname()).keyword(rsEvent2.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO2);
        //测试
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("djn牌猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("djn牌人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventname", is("djn牌人肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("djn牌狗肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventname", is("djn牌猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventname", is("djn牌人肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")))
                .andExpect(jsonPath("$[2].eventname", is("djn牌狗肉涨价了")))
                .andExpect(jsonPath("$[2].keyword", is("经济")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_update_a_rsevent() throws Exception {
        //创建用户并添加
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        //创建热搜并添加
        rsEvent rsEvent = new rsEvent("djn牌猪肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO);
        rsEvent rsEvent1 = new rsEvent("djn牌人肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO1 = RsEventPO.builder().eventname(rsEvent1.getEventname()).keyword(rsEvent1.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO1);
        rsEvent rsEvent2 = new rsEvent("djn牌狗肉涨价了","经济",saveduser.getId());
        RsEventPO rsEventPO2 = RsEventPO.builder().eventname(rsEvent2.getEventname()).keyword(rsEvent2.getKeyword())
                .userPO(saveduser).build();
        rsEventRepository.save(rsEventPO2);
        //准备要修改热搜
        rsEvent rsEvent_update = new rsEvent("djn牌牛肉涨价了","经济",saveduser.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent_update);
        mockMvc.perform(patch("/rs/1/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    public void should_add_rsevent_to_rslist_when_the_user_is_exist() throws Exception {
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent = new rsEvent("猪肉涨价了","经济",saveduser.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());
        assertEquals("猪肉涨价了",all.get(0).getEventname());
        assertEquals(saveduser.getId(),all.get(0).getUserPO().getId());
    }
    @Test
    public void should_return_badrequest_when_add_rsevent_and_the_user_not_exist() throws Exception {
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent = new rsEvent("猪肉涨价了","经济",2);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_delete_one_rsevent() throws Exception {
        //添加测试数据
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent = new rsEvent("djn牌猪肉涨价了","经济",saveduser.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        UserPO saveduser1 = userRepository.save(UserPO.builder()
                .name("gy").age(23).gender("female").phone("16666666666").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent1 = new rsEvent("gy牌猪肉涨价了","经济",saveduser1.getId());
        String jsonString1 = objectMapper.writeValueAsString(rsEvent1);
        mockMvc.perform(post("/rs/event").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //删除其中一个热搜
        mockMvc.perform(delete("/rs/2")).andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("djn牌猪肉涨价了",all.get(0).getEventname());
        assertEquals(saveduser.getId(),all.get(0).getUserPO().getId());
    }
    //error-handling作业需求，因为需求变更删掉了一个添加热搜时用户不规范测试。
    @Test
    public void should_throw_rsevent_not_valid_index_exception_when_get_and_delete() throws Exception {
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent = new rsEvent("猪肉涨价了","经济",saveduser.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //测试
        mockMvc.perform(get("/rs/0"))
                .andExpect(jsonPath("$.error",is("invalid index")))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/rs/0"))
                .andExpect(jsonPath("$.error",is("invalid index")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_throw_rsevent_not_valid_range_exception_when_get_list() throws Exception {
        //添加测试数据
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(22).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent = new rsEvent("djn牌猪肉涨价了","经济",saveduser.getId());
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        UserPO saveduser1 = userRepository.save(UserPO.builder()
                .name("gy").age(23).gender("female").phone("16666666666").email("c@d.com").votenumber(10).build());
        rsEvent rsEvent1 = new rsEvent("gy牌猪肉涨价了","经济",saveduser1.getId());
        String jsonString1 = objectMapper.writeValueAsString(rsEvent1);
        mockMvc.perform(post("/rs/event").content(jsonString1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //测试
        mockMvc.perform(get("/rs/list?start=0&end=8"))
                .andExpect(jsonPath("$.error",is("invalid request param")))
                .andExpect(status().isBadRequest());
    }
}
