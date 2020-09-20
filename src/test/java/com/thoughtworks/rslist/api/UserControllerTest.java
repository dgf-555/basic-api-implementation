package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import com.thoughtworks.rslist.po.*;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
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
    public void should_register_user() throws Exception {
        User user = new User("dgf","male",19,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPO> allUser = userRepository.findAll();
        assertEquals(1,allUser.size());
        assertEquals("dgf",allUser.get(0).getName());
        assertEquals("a@b.com",allUser.get(0).getEmail());
    }
    @Test
    public void should_get_user() throws Exception {
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("dgf").age(22).gender("male").phone("17777777777").email("a@b.com").votenumber(10).build());
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.name", is("dgf")))
                .andExpect(jsonPath("$.email", is("a@b.com")))
                .andExpect(status().isOk());
    }
    @Test
    public void name_should_less_than_8() throws Exception {
        User user = new User("dgfdgfdgf","male",19,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void age_should_less_than_100_and_more_than_18() throws Exception {
        User user = new User("dgf","male",13,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void email_format_should_be_true() throws Exception {
        User user = new User("dgf","male",19,"ab.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void phone_number_format_should_be_true() throws Exception {
        User user = new User("dgf","male",19,"a@b.com","188888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void gender_should_not_null() throws Exception {
        User user = new User("dgf",null,19,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_throw_exception_when_invalid_username() throws Exception {
        User user = new User("dgfdgfdgf","male",19,"a@b.com","18888888888");
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid user")))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void when_delete_user_should_delete_all_the_rsevent() throws Exception {
        //创建用户并添加
        UserPO saveduser = userRepository.save(UserPO.builder()
                .name("djn").age(19).gender("female").phone("17777777777").email("c@d.com").votenumber(10).build());
        UserPO saveduser1 = userRepository.save(UserPO.builder()
                .name("dgf").age(22).gender("male").phone("18888888888").email("a@b.com").votenumber(10).build());
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
        rsEvent rsEvent3 = new rsEvent("dgf牌猪肉涨价了","经济",saveduser1.getId());
        RsEventPO rsEventPO3 = RsEventPO.builder().eventname(rsEvent3.getEventname()).keyword(rsEvent3.getKeyword())
                .userPO(saveduser1).build();
        rsEventRepository.save(rsEventPO3);
        //删除
        mockMvc.perform(delete("/user/{id}",saveduser1.getId()))
                .andExpect(status().isOk());
        List<UserPO> all = userRepository.findAll();
        assertEquals(1,all.size());
        List<RsEventPO> all1 = rsEventRepository.findAll();
        assertEquals(3,all1.size());
    }
}