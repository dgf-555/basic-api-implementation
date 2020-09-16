package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

//    public List<User> initial_userList() {
//        List<User> userList = new ArrayList<>();
//        userList.add(new  User("dgf","male",19,"a@b.com","18888888888"));
//        return userList;
//    }

    @PostMapping("/user")
    public void add_user(@RequestBody @Valid User user){
        userList.add(user);
    }
    @GetMapping("/user")
    public List<User> get_userList(){
        return userList;
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
}
