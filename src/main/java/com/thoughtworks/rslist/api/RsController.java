package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.rsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//import static com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS.required;

@RestController
public class RsController {
  private List<rsEvent> rsList = initial_rsEvent();

  public static List<rsEvent> initial_rsEvent() {
    List<rsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new rsEvent("第一条事件","无标签"));
    rsEventList.add(new rsEvent("第二条事件","无标签"));
    rsEventList.add(new rsEvent("第三条事件","无标签"));
    return rsEventList;
  }
  @GetMapping("/rs/list")
  public List<rsEvent> get_a_list(@RequestParam(required=false) Integer start,@RequestParam(required=false) Integer end){
    if(start==null&&end==null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }
  @GetMapping("/rs/{index}")
  public rsEvent get_one_event(@PathVariable int index){
    return rsList.get(index-1);
  }
  @PostMapping("/rs/event")
  public void add_rsevent(@RequestBody rsEvent rsEvent){
    rsList.add(rsEvent);
  }
  @PatchMapping("/rs/{index}/event")
  public void update_rsevent(@PathVariable int index,@RequestBody rsEvent rsEvent){
    if(rsEvent.getEventname()!=null&&rsEvent.getKeyword()!=null){
      rsList.remove(index-1);
      rsList.add(index-1,rsEvent);
    }
    else if(rsEvent.getEventname()==null&&rsEvent.getKeyword()!=null){
      rsList.get(index-1).setKeyword(rsEvent.getKeyword());
    }
    else {
      rsList.get(index-1).setEventname(rsEvent.getEventname());
    }
  }
  @DeleteMapping("/rs/{index}")
  public void delete_rsevent(@PathVariable int index){
    rsList.remove(index-1);
  }

}
