package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//import static com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS.required;

@RestController
public class RsController {
  private List<rsEvent> rsList = initial_rsEvent();
  UserController userController = new UserController();//通过该类中的is_exist_username方法访问rslist数组

  public static List<rsEvent> initial_rsEvent() {
    List<rsEvent> rsEventList = new ArrayList<>();
    User user = new User("dgf","male",19,"a@b.com","18888888888");
    rsEventList.add(new rsEvent("第一条事件","无标签",user));
    rsEventList.add(new rsEvent("第二条事件","无标签",user));
    rsEventList.add(new rsEvent("第三条事件","无标签",user));
    return rsEventList;
  }
  @GetMapping("/rs/list")
  public ResponseEntity get_a_list(@RequestParam(required=false) Integer start,@RequestParam(required=false) Integer end){
    if(start==null&&end==null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }
  @GetMapping("/rs/{index}")
  public ResponseEntity<rsEvent> get_one_event(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index-1));
  }
  @PostMapping("/rs/event")
  public ResponseEntity add_rsevent(@RequestBody rsEvent rsEvent){
    //遍历热搜列表是不行的，需要遍历用户列表（因为可能添加了用户，但该用户没有写热搜）
    if(userController.is_exist_username(rsEvent.getUser())){
      userController.add_user(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    //在头部返回index值同时返回201
    HttpHeaders headers = new HttpHeaders();
    String index = String.valueOf(rsList.indexOf(rsEvent));
    headers.add("index", index);
    //return new ResponseEntity<>(headers, HttpStatus.CREATED);
    return ResponseEntity.status(HttpStatus.CREATED).header("index",index).body(null);
  }
  @PatchMapping("/rs/{index}/event")
  public ResponseEntity update_rsevent(@PathVariable int index,@RequestBody rsEvent rsEvent){
    if(rsEvent.getEventname()!=null&&rsEvent.getKeyword()!=null){
      rsList.set(index-1,rsEvent);
//      rsList.remove(index-1);
//      rsList.add(index-1,rsEvent);
    }
    else if(rsEvent.getEventname()==null&&rsEvent.getKeyword()!=null){
      rsList.get(index-1).setKeyword(rsEvent.getKeyword());
    }
    else {
      rsList.get(index-1).setEventname(rsEvent.getEventname());
    }
    return ResponseEntity.created(null).build();
  }
  @DeleteMapping("/rs/{index}")
  public ResponseEntity delete_rsevent(@PathVariable int index){
    rsList.remove(index-1);
    return ResponseEntity.created(null).build();
  }

}
