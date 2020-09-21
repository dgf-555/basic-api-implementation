package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
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

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user")
    public ResponseEntity add_user(@RequestBody @Valid User user){
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setGender(user.getGender());
        userPO.setPhone(user.getPhone());
        userPO.setVotenumber(user.getVotenumber());
        userRepository.save(userPO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user")
    public ResponseEntity get_userList(){
        return ResponseEntity.ok(userList);
    }
    @GetMapping("/user/{index}")
    public ResponseEntity get_one_user(@PathVariable int index){
        UserPO userfindbyId = userRepository.findById(index).get();
        User user = new User();
        user.setName(userfindbyId.getName());
        user.setAge(userfindbyId.getAge());
        user.setGender(userfindbyId.getGender());
        user.setEmail(userfindbyId.getEmail());
        user.setPhone(userfindbyId.getPhone());
        user.setVotenumber(userfindbyId.getVotenumber());
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity delete_user(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
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
