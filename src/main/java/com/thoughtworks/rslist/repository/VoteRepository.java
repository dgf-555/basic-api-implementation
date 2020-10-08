package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VotePO,Integer> {
    @Override
    List<VotePO> findAll();

    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);

    @Query(value = "select * from vote where local_date_time between :startTime and :endTime", nativeQuery = true)
    List<VotePO> findAllAccordingToStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
