package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserController.class);

//    public List<User> initial_userList() {
//        List<User> userList = new ArrayList<>();
//        userList.add(new  User("dgf","male",19,"a@b.com","18888888888"));
//        return userList;
//    }

    @PostMapping("/user")
    public void add_user(@RequestBody @Valid User user){
        //userList.add(user);
        //现在存到数据库中去了，不存在内存中了
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setGender(user.getGender());
        userPO.setPhone(user.getPhone());
        userPO.setVotenumber(user.getVotenumber());
        userRepository.save(userPO);
    }
    @GetMapping("/user")
    public List<User> get_userList(){
        return userList;
    }
    @GetMapping("/user/{index}")
    public User get_one_user(@PathVariable int index){
        UserPO userfindbyId = userRepository.findAll().get(index-1);
        User user = new User();
        user.setName(userfindbyId.getName());
        user.setAge(userfindbyId.getAge());
        user.setGender(userfindbyId.getGender());
        user.setEmail(userfindbyId.getEmail());
        user.setPhone(userfindbyId.getPhone());
        return user;
    }
    @DeleteMapping("/user/{index}")
    public void delete_user(@PathVariable int index){
        userRepository.deleteById(index);
    }

    public boolean is_exist_username(User user){
        boolean flag=false;
        for(int i=0;i<userList.size();i++){
            if(user.getName()==userList.get(i).getName()){
                flag = true;
                break;
            }
        }
        return flag;
    }
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ResponseEntity userExceptionHandler(){
        String errorMessage= "invalid user";
        Error error = new Error();
        error.setError(errorMessage);
        logger.error("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
}
