package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class RsController {
  Logger logger = LoggerFactory.getLogger(RsController.class);
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;
  //通过jdbc方式来和数据库连接并建表，用处不大，做示范用，以后不知道会不会有问题，如果太长了就删掉吧，rslist后期也会更改到存入数据库，到时候再改
//  private static void creat_table_by_jdbc() throws SQLException {
//    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rsSystem?user=root&password=dgf19981229&serverTimezone=Asia/Shanghai");
//    DatabaseMetaData metaData = connection.getMetaData();
//    ResultSet resultSet = metaData.getTables(null,null,"rsevent_create_by_jdbc",null);
//    if(!resultSet.next()){
//      String createTableSql = "create table rsevent_create_by_jdbc(eventName varchar(200) not null, keyword varchar(100) not null)";
//      Statement statement = connection.createStatement();
//      statement.execute(createTableSql);
//    }
//    connection.close();
//  }
  @GetMapping("/rs/list")
  public ResponseEntity get_a_list(@RequestParam(required=false) Integer start,@RequestParam(required=false) Integer end){
    if(start==null&&end==null){
      return ResponseEntity.ok(rsEventRepository.findAll());
    }
    if(start<1||start>rsEventRepository.findAll().size()||end<1||end>rsEventRepository.findAll().size()||start>end){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsEventRepository.findAll().subList(start-1,end));
  }
  @GetMapping("/rs/{index}")
  public ResponseEntity get_one_event(@PathVariable int index){
    if(index <1||index >rsEventRepository.findAll().size()){
      throw new RsEventNotValidException("invalid index");
    }
    RsEvent rsEvent =new RsEvent();
    RsEventPO rsEventPO = rsEventRepository.findById(index).get();
    rsEvent.setEventname(rsEventPO.getEventname());
    rsEvent.setKeyword(rsEventPO.getKeyword());
    rsEvent.setUserid(rsEventPO.getUserPO().getId());
    return ResponseEntity.ok(rsEvent);
  }
  @PostMapping("/rs/event")
  public ResponseEntity add_rsevent(@RequestBody @Valid RsEvent rsEvent){
    Optional<UserPO> userPO= userRepository.findById(rsEvent.getUserid());
    if(userRepository.existsById(rsEvent.getUserid())){
      RsEventPO buildfromrsEvent = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
              .userPO(userPO.get()).build();
      rsEventRepository.save(buildfromrsEvent);
      return ResponseEntity.status(HttpStatus.CREATED).header("index","添加热搜事件").build();
    }
    else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
  @PatchMapping("/rs/{index}/event")
  public ResponseEntity update_rsevent(@PathVariable int index,@RequestBody RsEvent rsEvent){
    Optional<UserPO> userPO= userRepository.findById(rsEvent.getUserid());
    RsEventPO buildfromrsEvent = RsEventPO.builder().eventname(rsEvent.getEventname()).keyword(rsEvent.getKeyword())
            .userPO(userPO.get()).id(index).build();
    rsEventRepository.save(buildfromrsEvent);
    return ResponseEntity.created(null).build();
  }
  @DeleteMapping("/rs/{index}")
  public ResponseEntity delete_rsevent(@PathVariable int index){
    if(rsEventRepository.existsById(index)){
      rsEventRepository.deleteById(index);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    throw new RsEventNotValidException("invalid index");
  }
  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity vote(@PathVariable int rsEventId, @RequestBody Vote vote){
    UserPO userPO = userRepository.findById(vote.getUserId()).get();
    RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
    if(vote.getVoteNum() <= userPO.getVotenumber()){
      userPO.setVotenumber(userPO.getVotenumber()-vote.getVoteNum());
      userRepository.save(userPO);
      rsEventPO.setVoteNum(vote.getVoteNum());
      rsEventRepository.save(rsEventPO);
      return ResponseEntity.ok(null);
    }
    else {
      return ResponseEntity.badRequest().build();
    }
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