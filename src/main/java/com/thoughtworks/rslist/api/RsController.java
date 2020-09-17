package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.rsEvent;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//import static com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS.required;

@RestController
public class RsController {
  private List<rsEvent> rsList = initial_rsEvent();
  UserController userController = new UserController();//通过该类中的is_exist_username方法访问rslist数组
  Logger logger = LoggerFactory.getLogger(RsController.class);

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
    if(start<1||start>rsList.size()||end<1||end>rsList.size()||start>end){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }
  @GetMapping("/rs/{index}")
  public ResponseEntity<rsEvent> get_one_event(@PathVariable int index){
    if(index <=0||index >rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
  }
  @PostMapping("/rs/event")
  public ResponseEntity add_rsevent(@RequestBody @Valid rsEvent rsEvent){
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
    if(index <=0||index >rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    rsList.remove(index-1);
    return ResponseEntity.created(null).build();
  }
  @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
  public ResponseEntity rsExceptionHandler(Exception e){
    String errorMessage;
    if(e instanceof MethodArgumentNotValidException){
      errorMessage = "invalid param";
    }
    else{
      errorMessage = e.getMessage();
    }
    Error error = new Error();
    error.setError(errorMessage);
    logger.error(errorMessage);
    return ResponseEntity.badRequest().body(error);
  }
}
