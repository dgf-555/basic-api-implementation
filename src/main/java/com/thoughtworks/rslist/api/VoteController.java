package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity getVoteRecordAccordingToUserIdAndRsEventId(@RequestParam(required = false) String startTime,
                                                                     @RequestParam(required = false) String endTime,
                                                                     @RequestParam(required = false) Integer rsEventId,
                                                                     @RequestParam(required = false) Integer userId,
                                                                     @RequestParam Integer pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex-1,5);
        if (startTime != null && endTime != null){
            return ResponseEntity.ok(
                voteRepository.findAllAccordingToStartTimeAndEndTime(LocalDateTime.parse(startTime), LocalDateTime.parse(endTime), pageable).stream().map(
                        item -> Vote.builder().userId(item.getUser().getId())
                                .localDateTime(item.getLocalDateTime())
                                .rsEventId(item.getRsEvent().getId())
                                .voteNum(item.getNum()).build()
                ).collect(Collectors.toList()));
        }
        if (rsEventId!=null && userId!=null ){
            return ResponseEntity.ok(
                    voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId,pageable).stream().map(
                            item -> Vote.builder().userId(item.getUser().getId())
                                    .localDateTime(item.getLocalDateTime())
                                    .rsEventId(item.getRsEvent().getId())
                                    .voteNum(item.getNum()).build()
                    ).collect(Collectors.toList()));
        }
        return ResponseEntity.badRequest().build();
    }
//    @GetMapping("/voteRecordAccordingToTime")
//    public ResponseEntity getVoteRecordAccordingToTime(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){
//        System.out.println(startTime);
//        return ResponseEntity.ok(
//                voteRepository.findAllAccordingToStartTimeAndEndTime(startTime,endTime).stream().map(
//                        item -> Vote.builder().userId(item.getUser().getId())
//                                .localDateTime(item.getLocalDateTime())
//                                .rsEventId(item.getRsEvent().getId())
//                                .voteNum(item.getNum()).build()
//                ).collect(Collectors.toList()));
//    }

}
