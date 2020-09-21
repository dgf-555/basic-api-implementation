package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Vote {
    private int rsEventId;
    private int userId;
    private int voteNum;
    private LocalDateTime votetime;
}
